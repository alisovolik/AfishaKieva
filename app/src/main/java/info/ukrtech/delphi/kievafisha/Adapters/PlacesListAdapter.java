package info.ukrtech.delphi.kievafisha.Adapters;

/**
 * Created by Delphi on 09.06.2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.FilmsItem;
import info.ukrtech.delphi.kievafisha.Shared.PlaceItem;
import info.ukrtech.delphi.kievafisha.Shared.RecyclerViewClickListener;


public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {
    private ArrayList<PlaceItem> itemsData;
    private RecyclerViewClickListener clickListener;
    public int focusedItem = 0;
    public Context mCont;
    private ViewHolder holder;


    public PlacesListAdapter(ArrayList<PlaceItem> itemsData, RecyclerViewClickListener listener, Context context) {
        this.itemsData = itemsData;
        this.clickListener = listener;
        this.mCont = context;

    }


    @Override
    public PlacesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_place_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, clickListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String name = "";
        if (itemsData.get(position).name != null) name = itemsData.get(position).description;

        String address = "";
        if (itemsData.get(position).address != null ) address = itemsData.get(position).description;
        viewHolder.txtViewTitle.setText(name);
        viewHolder.txtViewDescription.setText(address);

        //Картинка
        final String pic = itemsData.get(position).photo;
        if (pic != null && !pic.isEmpty()) {
            Picasso.with(mCont)
                    .load(pic)
                    .placeholder(R.drawable.ic_no_image)
                    .into(viewHolder.imgViewIcon);
        }


        viewHolder.itemView.setSelected(focusedItem == position);
        this.holder = viewHolder;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.idPlaceListTitle) TextView txtViewTitle;
        @BindView(R.id.idPlaceListDescription) TextView txtViewDescription;
        @BindView(R.id.idPlaceListLogo) ImageView imgViewIcon;
        public RecyclerViewClickListener listener;

        public ViewHolder(View itemLayoutView, RecyclerViewClickListener mListener) {
            super(itemLayoutView);
            listener = mListener;

            ButterKnife.bind(this, itemLayoutView);

            itemLayoutView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v)
        {
            notifyItemChanged(focusedItem);
            focusedItem = getLayoutPosition();
            notifyItemChanged(focusedItem);

            listener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }



    @Override
    public int getItemCount() {
        return itemsData.size();
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // Handle key up and key down and attempt to move selection
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }


    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }
}

