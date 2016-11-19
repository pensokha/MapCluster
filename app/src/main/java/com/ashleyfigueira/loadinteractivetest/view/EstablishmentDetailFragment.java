package com.ashleyfigueira.loadinteractivetest.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashleyfigueira.loadinteractivetest.R;
import com.ashleyfigueira.loadinteractivetest.geo.EstablishmentItem;
import com.ashleyfigueira.loadinteractivetest.model.Establishment;
import com.ashleyfigueira.loadinteractivetest.utils.Utils;

import org.w3c.dom.Text;


public class EstablishmentDetailFragment extends Fragment
{


    private CollapsingToolbarLayout appBarLayout;
    private ImageView establishmentImage;
    private TextView nameView;
    private TextView phoneView;
    private TextView addressView;
    private TextView descriptionView;

    private String name;
    private String phone;
    private String address;
    private String description;

    public EstablishmentDetailFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Utils.ESTABLISHMENT_NAME_KEY))
        {
            name = getArguments().getString(Utils.ESTABLISHMENT_NAME_KEY);
            phone = getArguments().getString(Utils.ESTABLISHMENT_PHONE_KEY);
            address = getArguments().getString(Utils.ESTABLISHMENT_ADDRESS_KEY);
            description = getArguments().getString(Utils.ESTABLISHMENT_DESCRIPTION_KEY);

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            establishmentImage = (ImageView) activity.findViewById(R.id.toolbar_establishment_image);
            if (appBarLayout != null)
            {
                appBarLayout.setTitle(name);
                //TODO - load any image into appbar with picasso
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_establishment_detail, container, false);
        nameView = (TextView) rootView.findViewById(R.id.name_text);
        phoneView = (TextView) rootView.findViewById(R.id.phone_text);
        addressView = (TextView) rootView.findViewById(R.id.address_text);
        descriptionView = (TextView) rootView.findViewById(R.id.description_text);

        nameView.setText(name);
        phoneView.setText(phone);
        addressView.setText(address);
        descriptionView.setText(description);

        return rootView;
    }


}
