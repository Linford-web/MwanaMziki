package com.example.eventmuziki.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserId());
    }


    public static CollectionReference allUsersCollection() {
        return FirebaseFirestore.getInstance().collection("Users");
    }

    public static DocumentReference getChatRoomReference(String chatRoomId) {
        return FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("Chats");
    }
    public static String getChatRoomId(String userId1, String userId2) {
        if (userId1.hashCode()<userId2.hashCode()){
            return userId1 +"_"+ userId2;
        }
        else {
            return userId2 +"_"+ userId1;
        }
    }

    public static CollectionReference allChatRoomCollections(){
        return FirebaseFirestore.getInstance().collection("ChatRooms");

    }

    public  static  DocumentReference getOtherUserFromChats(List<String> userIds){
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return  allUsersCollection().document(userIds.get(1));
        }else {
            return  allUsersCollection().document(userIds.get(0));
        }
    }
    public static String timeStampToString(Timestamp timeStamp){
        return  new SimpleDateFormat("HH:MM").format(timeStamp.toDate());
    }


}
