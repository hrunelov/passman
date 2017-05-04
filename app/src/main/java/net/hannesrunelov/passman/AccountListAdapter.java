package net.hannesrunelov.passman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private AccountInfo[] data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AccountInfoView view;
        public ViewHolder(AccountInfoView view) {
            super(view);
            this.view = view;
        }
    }

    public AccountListAdapter(AccountInfo[] data) {
        this.data = data;
    }

    @Override
    public AccountListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountInfoView view = (AccountInfoView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_info_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountInfo accountInfo = data[position];
        holder.view.setAccount(accountInfo.account);
        holder.view.setLength(accountInfo.length);
        holder.view.setInclude(accountInfo.include);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
