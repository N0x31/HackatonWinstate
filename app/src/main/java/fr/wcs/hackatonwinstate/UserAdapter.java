package fr.wcs.hackatonwinstate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by apprenti on 12/21/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserModel> mUserModelList;

    public UserAdapter(List<UserModel> moviesList) {
        this.mUserModelList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserModel userModel = mUserModelList.get(position);

        holder.textViewUserNameRV.setText(userModel.getUser_name());
    }

    @Override
    public int getItemCount() {
        return mUserModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUserNameRV;

        public MyViewHolder(View view) {
            super(view);
            textViewUserNameRV = view.findViewById(R.id.textViewUserNameRV);
        }
    }
}