package fr.wcs.hackatonwinstate;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adphi on 16/11/17.
 */

public class FirebaseHelper {
    private static FirebaseDatabase sDatabase = null;
    private static FirebaseStorage sStorage = null;
    private UploadImageListener mUploadImageListener = null;

    /**
     * Method to call for getting the Firebase Database.
     * It ensure that setPersistence is enabled at the first call to FirebaseDatabase.getInstance().
     * @return
     */
    public static FirebaseDatabase getDatabase(){
        if(sDatabase == null) {
            sDatabase = FirebaseDatabase.getInstance();
            //sDatabase.setPersistenceEnabled(true);
        }
        return sDatabase;
    }

    public static FirebaseStorage getsStorage(){
        if (sStorage == null){
            sStorage = FirebaseStorage.getInstance();
        }
        return sStorage;
    }

    /**
     * Private Constructor used to store listener when Sending Data to Firebase Storage.
     */
    private FirebaseHelper() {
    }

    /**
     * Helper Method uploading a Drawable to the Firebase's Avatar User Entry
     * @param drawable
     * @param userUid
     * @return the FirebaseHelper instance to set the UploadImageListener
     */
    public static FirebaseHelper uploadAvatar(Drawable drawable, String userUid) {
        StorageReference avatarReference = getsStorage().getReference("Avatars").child(userUid);
        return uploadImage(avatarReference, drawable);
    }

    /**
     * Helper Method uploading a Drawable to Firebase.
     * FirebaseHelper needs to be instantiated to be used.
     * @param reference where the drawable will be stored.
     * @param drawable the drawable to store.
     * @return the FirebaseHelper instance to set the UploadImageListener
     */
    public static FirebaseHelper uploadImage(StorageReference reference, Drawable drawable) {
        Bitmap image = ((BitmapDrawable) drawable).getBitmap();
        return uploadImage(reference, image);
    }

    /**
     * Helper Method uploading a Bitmap to Firebase.
     * @param reference
     * @param bitmap
     * @return the FirebaseHelper instance to set the UploadImageListener
     */
    public static FirebaseHelper uploadImage(StorageReference reference, Bitmap bitmap) {
        final FirebaseHelper fh = new FirebaseHelper();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri imageUri = taskSnapshot.getDownloadUrl();
                if(fh.mUploadImageListener != null) {
                    fh.mUploadImageListener.onSuccess(imageUri);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (fh.mUploadImageListener != null) {
                    fh.mUploadImageListener.onFailure(e.getMessage());
                }
            }
        });
        return fh;
    }

    /**
     * Sets the upload listener.
     * @param uploadImageListener
     */
    public void setUploadImageListener(UploadImageListener uploadImageListener) {
        mUploadImageListener = uploadImageListener;
    }

    public interface UploadImageListener {
        void onSuccess(Uri imageUri);
        void onFailure(String error);
    }

    /**
     * Helper Method to chain multiple Firebase Read requests.
     * @param references the references to be read.
     * @return The Request Object. You'll want to add a OnCompleteListener to it.
     */
    public static Request request(DatabaseReference... references) {
        // Security
        getDatabase();
        // Return a new Request Object to be able to directly set the listener
        return new Request(Arrays.asList(references));
    }

    public static Request request(ArrayList<DatabaseReference> references) {
        return new Request(references);
    }

    /**
     * The class handling a Firebase read chain requests. The class can't be instantiated directly.
     */
    public static class Request {
        // The references to read from
        private List<DatabaseReference> mReferences = new ArrayList<>();
        // The map storing read results
        private HashMap<DatabaseReference, DataSnapshot> mReferenceSnapshotMap = new HashMap<>();
        // The listener to be called when succeed or when failed
        private OnCompleteListener mOnCompleteListener = null;

        /**
         * Private Constructor, only used in FirebaseHelper.request(...)
         * @param references
         */
        private Request(List<DatabaseReference> references) {
            mReferences = references;
            // Initiate counter to know when returning reading results
            final int[] counter = new int[]{0};

            // Read all DatabaseReferences
            for(final DatabaseReference ref : mReferences) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Store the result
                        mReferenceSnapshotMap.put(ref, dataSnapshot);
                        counter[0] ++;
                        // Is it the last Read ?
                        if(counter[0] == mReferences.size() && mOnCompleteListener != null) {
                            // Notify and send results to the listener
                            mOnCompleteListener.onComplete(mReferenceSnapshotMap, null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Aie... Something went wrong
                        if(mOnCompleteListener != null) {
                            // Send the bad news
                            mOnCompleteListener.onComplete(mReferenceSnapshotMap, databaseError.toException());
                        }
                    }
                });
            }
        }

        /**
         * Sets the listener called when all the Requests are done or when request Failed.
         * @param listener
         */
        public void setOnCompleteListener(OnCompleteListener listener) {
            mOnCompleteListener = listener;
        }

        public interface OnCompleteListener {
            /**
             * Called after the last request in the chain succeed, or if a request failed
             * @param results the results map
             * @param e the exception if the reading chain request failed.
             */
            void onComplete(HashMap<DatabaseReference, DataSnapshot> results, @Nullable Exception e);
        }
    }
}
