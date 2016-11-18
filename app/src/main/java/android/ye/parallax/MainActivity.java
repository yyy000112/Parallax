package android.ye.parallax;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView listView;
    private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ParallaxListView) findViewById(R.id.ll_list);
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//永远不显示顶部下拉的蓝色阴影

        View headerView =View.inflate(this,R.layout.layout_header,null);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.iv_pic);
        listView.setParallaxImageView(imageView);
        listView.addHeaderView(headerView);

        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,indexArr));

    }
}
