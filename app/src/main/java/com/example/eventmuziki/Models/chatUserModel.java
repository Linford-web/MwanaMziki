package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class chatUserModel implements Parcelable {

    String  profilePicture, chatRoomId, userId, userName, email;

    public chatUserModel(String profilePicture, String chatRoomId, String userId, String userName, String email) {
        this.profilePicture = profilePicture;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;

    }

    public chatUserModel() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected chatUserModel(Parcel in) {
        profilePicture = in.readString();
        chatRoomId = in.readString();
        userId = in.readString();
        userName = in.readString();
        email = in.readString();
    }

    public static final Creator<chatUserModel> CREATOR = new Creator<chatUserModel>() {
        @Override
        public chatUserModel createFromParcel(Parcel in) {
            return new chatUserModel(in);
        }

        @Override
        public chatUserModel[] newArray(int size) {
            return new chatUserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(profilePicture);
        dest.writeString(chatRoomId);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(email);
    }
}


/*




 fetchChatRooms(chatRoomId);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        checkAndCreateChatRoom();

        //fetchChats(eventId);















  private void fetchChats(String eventId) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        fStore.collection("BookedEvents")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        chatUserModel event = documentSnapshot.toObject(chatUserModel.class);
                        chatsAdapters.add(event);
                    }
                    chatsAdapters.notifyDataSetChanged();
                });
    }

    private void checkAndCreateChatRoom() {
        // Check if the chat room already exists
        String chatRoomId = getIntent().getStringExtra("roomId");

        if (chatRoomId != null) {
            databaseReference.child(chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Chat room exists, fetch and display
                        fetchChatRooms(chatRoomId);
                    } else {
                        // Chat room does not exist, create a new one
                        //createNewChatRoom(chatRoomId);
                        Toast.makeText(chatActivity.this, "Could not fetch chat room", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(chatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void createNewChatRoom(String chatRoomId) {
        chatUserModel chatRoom = new chatUserModel("New Doc.",chatRoomId, FirebaseAuth.getInstance().getUid(), "", "" );
        databaseReference.child(chatRoomId).setValue(chatRoom).addOnSuccessListener(aVoid -> {
            fetchChatRooms(chatRoomId);
        }).addOnFailureListener(e -> {
            Toast.makeText(chatActivity.this, "Failed to create chat room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchChatRooms(String chatRoomId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String currentUserId = FirebaseAuth.getInstance().getUid();

        if (currentUserId == null) {
            Toast.makeText(chatActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(chatRoomId).child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // messageList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        chatUserModel chatRoom = dataSnapshot.getValue(chatUserModel.class);
                        if (chatRoom != null && (currentUserId.equals(chatRoom.getUserId()))) {
                            messageList.add(chatRoom);
                            Toast.makeText(chatActivity.this, "Chat rooms fetched", Toast.LENGTH_SHORT).show();
                        }
                    }

                    chatsAdapters.notifyDataSetChanged();
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);

                    if (messageList.isEmpty()) {
                        Toast.makeText(chatActivity.this, "No chat rooms found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(chatActivity.this, "Chat rooms fetched2", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(chatActivity.this, "No chat rooms found 2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }





 */

// chat roo files

/*





databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        eventId = getIntent().getStringExtra("eventId");
        bidderId = getIntent().getStringExtra("bidderId");
        creatorId = getIntent().getStringExtra("creatorId");
        musicianName = getIntent().getStringExtra("MusicianName");
        creatorName = getIntent().getStringExtra("OrganizerName");

        if (creatorId != null) {
            senderRoom = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + creatorId;
            receiverRoom = creatorId + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        } else {
            sendBtn = findViewById(R.id.sendMessageBtn);
            messageRecyclerView = findViewById(R.id.chatRecyclerView);
            messageText = findViewById(R.id.messageEditText);


            message = new messageAdapter(this, new ArrayList<>());
            messageRecyclerView.setAdapter(message);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            messageRecyclerView.setLayoutManager(layoutManager);

            dbReferenceSender = FirebaseDatabase.getInstance().getReference("Users").child(eventId).child("messages").child(senderRoom);
            getDbReferenceReceiver = FirebaseDatabase.getInstance().getReference("Users").child(eventId).child("messages").child(receiverRoom);

            dbReferenceSender.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<messageModel> messages = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        messageModel model = dataSnapshot.getValue(messageModel.class);
                        messages.add(model);
                    }
                    message.clearMessages();
                    for (messageModel model : messages){
                        message.addMessage(model);
                    }
                    message.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = messageText.getText().toString();
                if (messageTxt.trim().length()>0){
                    sendMessage(messageTxt);
                }
                else {
                    Toast.makeText(chatRoom.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });














        private void sendMessage(String messageTxt) {
        String messageId = UUID.randomUUID().toString();
        messageModel model = new messageModel(messageTxt, FirebaseAuth.getInstance().getCurrentUser().getUid(),"", messageId, System.currentTimeMillis());
        message.addMessage(model);

        dbReferenceSender.child(messageId).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(chatRoom.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });

        getDbReferenceReceiver.child(messageId).setValue(model);
        messageRecyclerView.scrollToPosition(message.getItemCount() - 1);
        messageText.setText("");

    }
 */