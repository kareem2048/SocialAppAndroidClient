package com.example.kemo.socializer.Control;

import com.example.kemo.socializer.Connections.MainServerConnection;
import com.example.kemo.socializer.SocialAppGeneral.Command;
import com.example.kemo.socializer.SocialAppGeneral.LoginInfo;
import com.example.kemo.socializer.SocialAppGeneral.SocialArrayList;
import com.example.kemo.socializer.SocialAppGeneral.UserInfo;

import java.util.ArrayList;

/**
 * Created by kemo on 24/12/2016.
 */
public class ClientLoggedUser {
    public static final String ADD_FRIEND="add_Friend";
    private static final String GET_FRIENDS="get_Friends";
    public static final String FRIEND_REQ = "friend_req";
    public static final String FETCH_REQS = "fetch_reqs";
    public static final String GET_RELATION_STATUS = "get_relation_status";
    public static final String ACCEPT_FRIEND = "accept_friend_req";
    public static final String DECLINE_FRIEND = "decline_friend_req";
    public static final String REMOVE_FRIEND = "remove_friend";
    public static final String CANCEL_FRIEND_REQ = "cancel_friend_req";
    public static final String DEACTIVATE = "deactivate";
    public static final String REACTIVATE = "reactivate";
    public static String id;
    public static abstract class Login
    {
        protected Login(LoginInfo loginInfo)
        {
            Command command = new Command();
            command.setKeyWord(LoginInfo.NEW_LOGIN);
            command.setSharableObject(loginInfo.convertToJsonString());
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command cmd) {
                    onFinish(cmd.getObjectStr());
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(String id);
    }
    public static abstract class GetFriends
    {
        public GetFriends()
        {
            Command command = new Command();
            command.setKeyWord(GET_FRIENDS);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command cmd) {

                    onFinish((ArrayList<String>) SocialArrayList.convertFromJsonString(cmd.getObjectStr()).getItems());
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(ArrayList<String> ids);
    }
    public static abstract class GetFriendInfo
    {
        protected GetFriendInfo(String id)
        {
            Command command = new Command();
            command.setKeyWord(UserInfo.PICK_INFO);
            command.setSharableObject(id);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
                @Override
                public void analyze(Command cmd) {
                    UserInfo userInfo = UserInfo.fromJsonString(cmd.getObjectStr());
                    pick(userInfo);
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void pick(UserInfo userInfo);
    }
}