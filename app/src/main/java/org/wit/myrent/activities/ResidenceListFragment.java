package org.wit.myrent.activities;

import java.util.ArrayList;
import org.wit.android.helpers.IntentHelper;
import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView.OnItemClickListener;

public class ResidenceListFragment extends ListFragment implements OnItemClickListener
{
  private ArrayList<Residence> residences;
  private Portfolio portfolio;
  private ResidenceAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    getActivity().setTitle(R.string.app_name);

    MyRentApp app = (MyRentApp) getActivity().getApplication();
    portfolio = app.portfolio;
    residences = portfolio.residences;

    adapter = new ResidenceAdapter(getActivity(), residences);
    setListAdapter(adapter);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
  {
    View v = super.onCreateView(inflater, parent, savedInstanceState);
    return v;
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    Residence res = ((ResidenceAdapter) getListAdapter()).getItem(position);
    Intent i = new Intent(getActivity(), ResidenceActivity.class);
    i.putExtra(ResidenceFragment.EXTRA_RESIDENCE_ID, res.id);
    startActivityForResult(i,0);
  }

  @Override
  public void onResume()
  {
    super.onResume();
    ((ResidenceAdapter) getListAdapter()).notifyDataSetChanged();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
  {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.residencelist, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.menu_item_new_residence:
        Residence residence = new Residence();
        portfolio.addResidence(residence);

        Intent i = new Intent(getActivity(), ResidenceActivity.class);
        i.putExtra(ResidenceFragment.EXTRA_RESIDENCE_ID, residence.id);
        startActivityForResult(i, 0);
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
  {
    Residence residence = adapter.getItem(position);
    IntentHelper.startActivityWithData(getActivity(), ResidenceActivity.class, "RESIDENCE_ID", residence.id);
  }

  class ResidenceAdapter extends ArrayAdapter<Residence>
  {
    private Context context;

    public ResidenceAdapter(Context context, ArrayList<Residence> residences)
    {
      super(context, 0, residences);
      this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      if (convertView == null)
      {
        convertView = inflater.inflate(R.layout.list_item_residence, null);
      }
      Residence res = getItem(position);

      TextView geolocation = (TextView) convertView.findViewById(R.id.residence_list_item_geolocation);
      geolocation.setText(res.geolocation);

      TextView dateTextView = (TextView) convertView.findViewById(R.id.residence_list_item_dateTextView);
      dateTextView.setText(res.getDateString());

      CheckBox rentedCheckBox = (CheckBox) convertView.findViewById(R.id.residence_list_item_isrented);
      rentedCheckBox.setChecked(res.rented);

      return convertView;
    }
  }
}