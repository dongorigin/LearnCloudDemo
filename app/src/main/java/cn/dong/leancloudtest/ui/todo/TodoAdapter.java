package cn.dong.leancloudtest.ui.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cn.dong.leancloudtest.AVHelper;
import cn.dong.leancloudtest.R;
import cn.dong.leancloudtest.model.Todo;

/**
 * @author dong on 15/7/11.
 */
class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    List<Todo> mTodos = new ArrayList<>();

    public void setTodos(List<Todo> todos) {
        mTodos.clear();
        mTodos.addAll(todos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TodoViewHolder holder, int position) {
        final Todo todo = mTodos.get(position);
        holder.titleView.setText(mTodos.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog(holder.itemView.getContext(), todo);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteDialog(holder.itemView.getContext(), todo);
                return true;
            }
        });
    }

    private void updateDialog(Context context, final Todo todo) {
        final EditText inputView = new EditText(context);
        inputView.setText(todo.getTitle());
        inputView.setSelection(inputView.getText().length());
        new AlertDialog.Builder(context)
                .setTitle(R.string.update)
                .setView(inputView)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String input = inputView.getText().toString().trim();
                        AVHelper.updateTodo(todo.getObjectId(), input, new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    todo.setTitle(input);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                })
                .show();

    }

    private void deleteDialog(Context context, final Todo todo) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete)
                .setMessage(todo.getTitle())
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AVHelper.deleteTodo(todo, new DeleteCallback() {
                            @Override
                            public void done(AVException e) {
                                mTodos.remove(todo);
                                notifyDataSetChanged();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;

        public TodoViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
