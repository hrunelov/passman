package net.hannesrunelov.seedpass;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Set;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {
    private Set<Service> services;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ServiceListItemView view;
        public ViewHolder(ServiceListItemView view) {
            super(view);
            this.view = view;
        }
    }

    public ServiceListAdapter(Set<Service> services) {
        this.services = services;
    }

    @Override
    public ServiceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ServiceListItemView view = (ServiceListItemView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list_item_view, parent, false);

        view.setServiceChangedListener(new ServiceListItemView.ServiceChangedListener() {
            @Override
            public void onServiceChanged(Service oldService, Service newService) {
                int oldPosition = positionOf(oldService);
                int oldSize = services.size();
                services.remove(oldService);
                services.add(newService);
                if (services.size() < oldSize) {
                    notifyItemRemoved(oldPosition);
                } else {
                    int newPosition = positionOf(newService);
                    if (oldPosition != newPosition) notifyItemMoved(oldPosition, newPosition);
                    notifyItemChanged(newPosition);
                }

                // Save
                PreferencesHelper.write((Activity)view.getContext(), services);
            }
        });

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setService(getServiceArray()[position]);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    private Service[] getServiceArray() {
        Service[] result = new Service[services.size()];
        int i = 0;
        for (Service s : services) {
            result[i++] = s;
        }
        return result;
    }

    public int positionOf(Service service) {
        Service[] arr = getServiceArray();
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i].equals(service)) return i;
        }
        return -1;
    }

    public Service serviceAt(int position) {
        if (position < 0 || position >= services.size()) return null;
        return getServiceArray()[position];
    }
}
