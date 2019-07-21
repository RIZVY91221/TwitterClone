package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTwitteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText twitte_Text;
    private Button send_Twitte_Text;

    private ListView listView;
    private Button viewTwitte;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twitte);

        twitte_Text=findViewById(R.id.edt_twitte_text);
        send_Twitte_Text=findViewById(R.id.btn_send);

        listView=findViewById(R.id.listView);
        viewTwitte=findViewById(R.id.showPost);

        viewTwitte.setOnClickListener(this);


        send_Twitte_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject parseObject=new ParseObject("MyTweet");
                parseObject.put("twitte",twitte_Text.getText().toString()+"");
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                dialog = new ProgressDialog(SendTwitteActivity.this);
                dialog.setMessage("Sending...");
                dialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            FancyToast.makeText(SendTwitteActivity.this,"Send Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            dialog.dismiss();
                        }
                        else {
                            FancyToast.makeText(SendTwitteActivity.this,e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
       final ArrayList<HashMap<String, String>> twitteList=new ArrayList<>();
        final SimpleAdapter adapter=new SimpleAdapter(SendTwitteActivity.this,twitteList,android.R.layout.simple_list_item_2
                ,new String[]{"tweetUserName","tweetValue"},
                new int[]{android.R.id.text1,android.R.id.text2});

        try{

            ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("username",ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size()>0 && e==null){
                        for (ParseObject twitterObj:objects){

                            HashMap<String, String> userTweet=new HashMap<>();
                            userTweet.put("tweetUserName",twitterObj.getString("username"));
                            userTweet.put("tweetValue",twitterObj.getString("twitte"));

                            twitteList.add(userTweet);
                        }
                        listView.setAdapter(adapter);

                    }
                    else {
                        Log.i("Tag",e.getMessage()+"");
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
