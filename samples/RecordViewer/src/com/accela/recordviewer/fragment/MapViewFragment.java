package com.accela.recordviewer.fragment;


import java.util.ArrayList;
import java.util.List;

import com.accela.recordviewer.ApplicationEx;
import com.accela.recordviewer.R;
import com.accela.recordviewer.RecordService;
import com.accela.recordviewer.model.RecordModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MapViewFragment extends Fragment implements 
OnMarkerClickListener, OnInfoWindowClickListener {

	private MapView mapView;
	private GoogleMap map;
	private RecordService recordService;
	
	OnMakerClickListener makerClickListener;
	private ArrayList<Marker> markerList = new ArrayList<Marker>();

	 public interface  OnMakerClickListener{
	        /**
	         * Callback for when an item has been selected.
	         */
	        public void onRecordSelected(int id);
	 }

	
	Handler handler = new Handler() {
		
	};
	
	
    public MapViewFragment() {
    	
    }
    
    public void setOnMakerClickListener(OnMakerClickListener l) {
    	makerClickListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapview,container, false);
        
        
        
        ApplicationEx application = (ApplicationEx) getActivity().getApplication();
        recordService = application.getRecordService();
        
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        setupMap();
        addMakerToMap();
        return view;
    }

    private boolean checkMap() {
    	if(map == null) {
    		map = mapView.getMap();
    		if(map!=null) {
    			MapsInitializer.initialize(this.getActivity());
    		}
    	}
    	return map!=null;
    	
    }
    
    private void setupMap() {
    	if(!checkMap()) {
    		return;
    	}
    	map.getUiSettings().setZoomControlsEnabled(false);
    	map.setOnMarkerClickListener(this);
    	map.setOnInfoWindowClickListener(this);
    	
    }
    
    private void addMakerToMap() {
    	if(!checkMap()) {
    		return;
    	}
    	map.clear();
    	markerList.clear();
    	final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int markerCount = 0;
    	List<RecordModel> listRecords = recordService.getRecordList();
    	for(RecordModel record: listRecords) {
    		if(record.address==null)
    			continue;
    		MarkerOptions options = new MarkerOptions();
    		LatLng latlng = new LatLng(record.address.xCoordinate, record.address.yCoordinate);
    		options.position(latlng);
    		options.title(record.type);
    		options.snippet(record.address.getAddress());
    		
    		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.report_graffiti);
    		options.icon(BitmapDescriptorFactory.fromBitmap(bitmap
    				));
    				
    		markerCount++;
    		Marker marker = map.addMarker(options);
    		markerList.add(marker);
    		builder.include(latlng);
    	}
    	

    	if(markerCount>0) {
    		handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
				}
        		
        	}, 800);
    		
    	}
    	
    }
    
    @Override
	public void onInfoWindowClick(Marker marker) {
    	if(makerClickListener!=null) {
    		int index = markerList.indexOf(marker);
    		makerClickListener.onRecordSelected(index);
    	}
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
    
    
	
	public void refresh() {
	//	setupMap();
        addMakerToMap();
	}
    
	@Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        checkMap();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
    	mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    
}