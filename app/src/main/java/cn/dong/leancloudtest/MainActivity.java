package cn.dong.leancloudtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cn.dong.leancloudtest.model.Todo;
import cn.dong.leancloudtest.ui.BaseActivity;
import cn.dong.leancloudtest.util.L;

public class MainActivity extends BaseActivity {
    View mRootView;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    FloatingActionButton mFAB;

    TodoAdapter mAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVAnalytics.trackAppOpened(getIntent());

        mRootView = findViewById(R.id.root);
        setupFAB();
        setupRefreshLayout();
        setupRecyclerView();
        updateTodos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AVUser.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupFAB() {
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });
    }

    private void setupRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primaryDark);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTodos();
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addDialog() {
        final EditText inputView = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("添加Todo")
                .setView(inputView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTodo(inputView.getText().toString());
                    }
                })
                .show();
    }

    private void createTodo(String title) {
        AVHelper.createTodo(title, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    updateTodos();
                    Snackbar.make(mRootView, "添加成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    L.d(TAG, e.getMessage());
                    Snackbar.make(mRootView, "添加失败", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTodos() {
        AVHelper.fetchTodos(new FindCallback<Todo>() {
            @Override
            public void done(List<Todo> list, AVException e) {
                if (e == null) {
                    mAdapter.setTodos(list);
                    mRefreshLayout.setRefreshing(false);
                } else {

                }
            }
        });
    }

    static class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
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

    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;

        public TodoViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
