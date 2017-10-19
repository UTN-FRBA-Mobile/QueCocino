package com.mobile.utn.quecocino.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

import java.util.List;

public class SlidingMenuAdapter extends BaseAdapter {

    private Context context;
    private List<ItemSlideMenu> items;

    public SlidingMenuAdapter(Context context, List<ItemSlideMenu> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.item_sliding_menu, null);
        ImageView img = (ImageView) v.findViewById(R.id.item_img);
        TextView text = (TextView) v.findViewById(R.id.item_title);

        ItemSlideMenu item = items.get(i);
        img.setImageResource(item.getImagen());
        text.setText(item.getTitulo());

        return v;
    }
}
