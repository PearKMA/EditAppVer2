package groups.kma.editappver2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import groups.kma.editappver2.R;
import groups.kma.editappver2.model.MenuAction;

public class ListGrid extends RecyclerView.Adapter<ListGrid.ViewHolder>{
    private CallbackInterfaceDialog mCallback;
    private ArrayList<Integer> listMenu;
    private Context context;

    public ListGrid(Context context, ArrayList<Integer> listMenu)
    {
        this.context=context;
        this.listMenu=listMenu;
        try{
            mCallback = (CallbackInterfaceDialog) context;
        }catch(ClassCastException ex){
            Log.e("MyAdapter","Must implement the CallbackInterface in the Activity", ex);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid,parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.img.setImageResource(listMenu.get(position));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null){
                    mCallback.onSelection(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public interface CallbackInterfaceDialog{
        void onSelection(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.imgGrid);
        }
    }
}
