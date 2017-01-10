package com.example.kemo.socializer.View.MainActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.kemo.socializer.Connections.ReceiveServerNotification;
import com.example.kemo.socializer.Connections.ServerNotFound;
import com.example.kemo.socializer.Connections.TransmissionFailureListener;
import com.example.kemo.socializer.Connections.UtilityConnection;
import com.example.kemo.socializer.Control.ClientLoggedUser;
import com.example.kemo.socializer.R;
import com.example.kemo.socializer.SocialAppGeneral.Command;
import com.example.kemo.socializer.SocialAppGeneral.Notification;
import com.example.kemo.socializer.SocialAppGeneral.SocialArrayList;
import com.example.kemo.socializer.View.FragmentNavigator;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContentFragment extends Fragment implements FragmentNavigator {
    private HomeFragment homeFragment;
    private FriendsFragment friendsFragment;
    private NotificationFragment notificationFragment;
    public ContentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragment = new HomeFragment();
        friendsFragment = new FriendsFragment();
        notificationFragment = new NotificationFragment();
        startNotifications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        view.findViewById(R.id.home_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(homeFragment);
            }
        });
        view.findViewById(R.id.friends_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {navigate(friendsFragment);}});
        view.findViewById(R.id.notification_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {navigate(notificationFragment);}});
        return view;
    }
//     <T> T myfun(Class<T> classOfT) //important
//    {
//        Object o;
//       return Primitives.wrap(classOfT).cast(o);
//    }
    @Override
    public void navigate(final Fragment fragment) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sub_container,
                                fragment)
                        .commit();
            }
        });

    }
    private void startNotifications()
    {
        final int PORT_NO = 6100;
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    try {
                        final ReceiveServerNotification receiveServerCommand = new ReceiveServerNotification(
                                new UtilityConnection(ClientLoggedUser.id, PORT_NO) {
                                    //TODO #Polymorphism #override
                                    @Override
                                    public void startConnection() {
                                        super.startConnection();
                                        new ClientLoggedUser.LoadNotification() {
                                            @Override
                                            public void onFinish(SocialArrayList list) {
                                                for ( String o : list.getItems()
                                                        ) {
                                                    notificationFragment.addNotification(Notification.fromJsonString(o));
                                                }
                                            }
                                        };
                                    }
                                }
                                        .getConnectionSocket()) {
                            @Override
                            public void Analyze(Command command) {
                                if (command.getKeyWord().equals(Notification.NEW_NOTIFICATION))
                                {
                                    notificationFragment.addNotification(Notification.fromJsonString(command.getObjectStr()));
                                }
                            }
                        };
                        receiveServerCommand.setTransmissionFailureListener(new TransmissionFailureListener() {
                            @Override
                            public void onDisconnection() {
                                try {
                                    receiveServerCommand.getRemote().close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    receiveServerCommand.setRemote(new UtilityConnection(ClientLoggedUser.id, PORT_NO).getConnectionSocket());
                                } catch (ServerNotFound serverNotFound) {
                                    serverNotFound.printStackTrace();
                                }
                            }
                        });
                        receiveServerCommand.start();
                    } catch (ServerNotFound ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
