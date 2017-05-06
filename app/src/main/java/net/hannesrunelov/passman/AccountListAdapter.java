package net.hannesrunelov.passman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private List<String> accounts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AccountListItemView view;
        public ViewHolder(AccountListItemView view) {
            super(view);
            this.view = view;
        }
    }

    public AccountListAdapter(List<String> accounts) {
        this.accounts = accounts;
    }

    @Override
    public AccountListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountListItemView view = (AccountListItemView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_list_item_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setAccount(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}
