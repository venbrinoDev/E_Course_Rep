package com.example.reno.ui.home;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reno.AboutActivity;
import com.example.reno.AddNumber;
import com.example.reno.DiologsendActivity;
import com.example.reno.HomeActivity;
import com.example.reno.MessageCredit;
import com.example.reno.MessageHistory;
import com.example.reno.ProfileActivity;
import com.example.reno.R;
import com.example.reno.Recyker;
import com.example.reno.ui.send.SendViewModel;
import com.example.reno.ui.slideshow.SlideshowFragment;
import com.example.reno.ui.tools.ToolsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }
    public TextView balance ;
    public long backPressedTime;
    public Toast backToast;
  public CardView cardView;
  public ImageView ReminderButton;
  public  GridLayout gridLayout;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);


        balance = root.findViewById(R.id.balancevalue);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (backPressedTime + 2000 > System.currentTimeMillis()){
                    backToast.cancel();
                    System.exit(0);
                    return;
                }
                else {
                    backToast = Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_LONG);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
        }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        gridLayout = root.findViewById(R.id.mainGrid);
        setEvent(gridLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String number = String.valueOf(dataSnapshot.child("currentbal").getValue());

                TextView balance = root.findViewById(R.id.balancevalue);

                balance.setText("Balance: " + number+ " NGN");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
Runnable runnable= new Runnable() {
    @Override
    public void run() {
        ReminderButton=root.findViewById(R.id.RemindetButton);
        Animation animationUtils= AnimationUtils.loadAnimation(getContext(),R.anim.shake);
        ReminderButton.startAnimation(animationUtils);

        ReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent remiderClass=new Intent(getActivity(), Recyker.class);
                startActivity(remiderClass);
            }
        });

    }
};
runnable.run();
        return root;
    }

public void setEvent(GridLayout gridLayout) {
    for (int a = 0; a < gridLayout.getChildCount();a++){
        cardView = (CardView)gridLayout.getChildAt(a);
         final int finali = a;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
               if(finali ==0){
                   DiologsendActivity diologsendActivity = new DiologsendActivity();
                   diologsendActivity.show(getFragmentManager(),"dialog send");
               }else if(finali ==1){
                   Intent intent = new Intent(getActivity(), MessageHistory.class);
                   startActivity(intent);

               }else if(finali == 2){
                   Intent intent = new Intent(getActivity(), MessageCredit.class);
                   startActivity(intent);

               }else if(finali == 3){
                   Intent intent = new Intent(getActivity(), AddNumber.class);
                   startActivity(intent);
                }

            }
        });
    }
}

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}