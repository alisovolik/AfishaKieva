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

import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.FilmsItem;
import info.ukrtech.delphi.kievafisha.Shared.RecyclerViewClickListener;


public class FilmsListAdapter extends RecyclerView.Adapter<FilmsListAdapter.ViewHolder> {
    private ArrayList<FilmsItem> itemsData;
    private RecyclerViewClickListener clickListener;
    public int focusedItem = 0;
    public Context mCont;
    private ViewHolder holder;


    public FilmsListAdapter(ArrayList<FilmsItem> itemsData, RecyclerViewClickListener listener, Context context) {
        this.itemsData = itemsData;
        this.clickListener = listener;
        this.mCont = context;

    }


    @Override
    public FilmsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_films_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, clickListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String title = "";
        if (itemsData.get(position).title != null) title = itemsData.get(position).title;

        String text = "";
        if (itemsData.get(position).text != null ) text = itemsData.get(position).text;
        viewHolder.txtViewTitle.setText(title);
        viewHolder.txtViewDescription.setText(text);

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

        public TextView txtViewTitle;
        public TextView txtViewDescription;
        public ImageView imgViewIcon;
        public RecyclerViewClickListener listener;

        public ViewHolder(View itemLayoutView, RecyclerViewClickListener mListener) {
            super(itemLayoutView);
            listener = mListener;
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.idFilmListTitle);
            txtViewDescription = (TextView) itemLayoutView.findViewById(R.id.idFilmListDescription);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.idFilmListLogo);
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

