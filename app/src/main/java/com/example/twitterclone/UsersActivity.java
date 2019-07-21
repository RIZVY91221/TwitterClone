package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private String follow_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        FancyToast.makeText(this,"Wellcome " +ParseUser.getCurrentUser().get("username"), Toast.LENGTH_LONG,FancyToast.INFO,true).show();

        listView=findViewById(R.id.list_user);
        final TextView textView=findViewById(R.id.loding_text);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);



        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());


        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size()>0 && e==null){
                    for (ParseUser user:objects){
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                    for (String user:arrayList){
                        if (ParseUser.getCurrentUser().getList("fanOf") !=null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(user)) {
                                follow_user = follow_user + user;
                                listView.setItemChecked(arrayList.indexOf(user), true);
                                FancyToast.makeText(UsersActivity.this, ParseUser.getCurrentUser().getUsername() + follow_user + " is Followed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            }
                        }
                    }
                    textView.animate().alpha(0).setDuration(2000);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id)
    {
        CheckedTextView checkedTextView= (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(UsersActivity.this,arrayList.get(position)+" now Followed", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        }else {
            FancyToast.makeText(UsersActivity.this,arrayList.get(position)+" Unfollowed ", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserFanOf= ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOf);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(UsersActivity.this,"Saved", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }else {
                    FancyToast.makeText(UsersActivity.this,e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_logout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent=new Intent(UsersActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.twittePage:
                Intent intent =new Intent(UsersActivity.this,SendTwitteActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
