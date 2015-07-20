package webservices.jibin.com.webservicesstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jibin on 20/07/15.
 */
public class ListAdapter extends BaseAdapter {

    ArrayList<User> users;
    Context context;

    public ListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_item, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.userId);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView body = (TextView) rowView.findViewById(R.id.body);

        User user = users.get(position);
        id.setText(user.getId());
        title.setText(user.getTitle());
        body.setText(user.getBody());


        return rowView;
    }
}
