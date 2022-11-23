package com.app.appgreco;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.SearchPlaceAdapterViewHolder> implements Filterable {

    Context mCntx;
    public List<GenericModel> arrayList;
    public List<GenericModel> arrayListFiltered;
    private IModalExtra listener;
    private final DatabaseReference mReactionsRef= FirebaseDatabase.getInstance().getReference()
            .child("Reacciones").child("id_proyectos");

    private long nroMeGusta = 0;
    private long nroMeEncanta = 0;
    private long nroMeAsombra = 0;
    private Integer[] reactionsQuantity = {0, 0, 0};
    private final String[] reaction_keys = {
            "me_gusta", "me_encanta", "me_asombra"
    };
    private final String[] strings = {
            "me gusta", "me encanta", "wow"
    };

    public GenericAdapter(Context mCntx, List<GenericModel> arrayList, IModalExtra listener)
    {
        this.mCntx = mCntx;
        this.arrayList = arrayList;
        arrayListFiltered = arrayList;
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public SearchPlaceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_noticias, parent, false);

        SearchPlaceAdapterViewHolder viewHolder = new SearchPlaceAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchPlaceAdapterViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        holder.titulo_noticia.setText(arrayListFiltered.get(position).getTitulo());
        holder.descripcion_noticia.setText(arrayListFiltered.get(position).getAutor());

        holder.imagenUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), DetalleActivity.class));
                listener.modalIniciarDetail(arrayList.get(position).getId());
                Constants.FORO = arrayList.get(position);

            }
        });
        String uID = "";

        try{
            if(FirebaseAuth.getInstance().getCurrentUser().getUid() != null){
                uID =FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }catch (Exception e){
            uID = "xxx";

        }

    }

    public class SearchPlaceAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView titulo_noticia;
        public TextView descripcion_noticia;
        public ImageView imagenUrl;

        //
        public ImageView mReaction_icon;
        public ImageView reactionImage; // txt_nro_reactions
        public ImageView meGustaImage; // txt_nro_reactions
        public ImageView meEncantaImage; // txt_nro_reactions
        public  ImageView meAsombraImage; // txt_nro_reactions
        public  TextView nroReactions;


        public SearchPlaceAdapterViewHolder(View itemView) {
            super(itemView);
            titulo_noticia = (TextView) itemView.findViewById(R.id.titulo_noticia);
            descripcion_noticia = (TextView) itemView.findViewById(R.id.descripcion_noticia);
            imagenUrl = (ImageView) itemView.findViewById(R.id.imagenUrl);

            reactionImage = itemView.findViewById(R.id.reaction_icon);
            nroReactions = itemView.findViewById(R.id.txt_nro_reactions);
            meGustaImage = itemView.findViewById(R.id.me_gusta_icon);
            meEncantaImage = itemView.findViewById(R.id.me_encanta_icon);
            meAsombraImage = itemView.findViewById(R.id.me_asombra_icon);
        }
    }

    public Filter getFilter()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<GenericModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 || constraint == "") {

                filteredList.addAll(arrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (GenericModel item : arrayList) {
                  //  if (item.getTitle().toLowerCase().contains(filterPattern)) {
                    //    filteredList.add(item);
                    //}
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}