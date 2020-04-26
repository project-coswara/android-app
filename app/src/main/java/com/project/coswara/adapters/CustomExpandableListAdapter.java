package com.project.coswara.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.project.coswara.BuildConfig;
import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.activities.RecordAudioActivity;
import com.project.coswara.RecordWaveTask;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "RecordExpandListAdapter";
    private final Context context;
    private final List<String> optionsList;
    private final List<String> valuesList;
    private final List<String> instrList;

    private final int STATE_NOT_STARTED = 0;
    private final int STATE_RECORDING_START = 1;
    private final int STATE_RECORDING_FINISH = 2;
    private final int STATE_UPLOADING = 3;

    private final String ABSOLUTE_PATH;
    private int state = STATE_NOT_STARTED; //0 = not started, 1 = recording, 2 = recorded, 3 = uploading
    private LinearLayout beforeRecordLyt, afterRecordLyt, uploadRecordLyt;
    private Button startRecordingBtn;
    private Button playSampleBtn;
    private boolean samplePlayStatus = false, recordPlayStatus = false;

    private String filePath = "";

    private RecordWaveTask recordTask = null;

    private final StorageReference storageRef;
    private final String uid;

    private int currGroupPos = 0;
    private final RecordAudioActivity.ExpandGroupInterface callback;
    private TextView instructionText;

    private final boolean[] statusList;
    private SimpleExoPlayer samplePlayer = null, recordPlayer = null;
    private Button playRecordingBtn;
    private Button uploadRecordingBtn;
    private Button recordAgainBtn;

    private final FirebaseFirestore db;
    private final RecordAudioActivity.FinishRecordInterface finishCallback;
    private UploadTask uploadTask = null;

    public CustomExpandableListAdapter(Context context, List<String> optionsList, List<String> valuesList,
                                       List<String> instrList, boolean[] statusList,
                                       String ABSOLUTE_PATH,
                                       StorageReference storageRef, String uid,
                                       FirebaseFirestore db,
                                       RecordAudioActivity.ExpandGroupInterface callback,
                                       RecordAudioActivity.FinishRecordInterface finishCallback) {
        this.context = context;
        this.optionsList = optionsList;
        this.valuesList = valuesList;
        this.statusList = statusList;
        this.ABSOLUTE_PATH = ABSOLUTE_PATH;
        this.instrList = instrList;
        this.storageRef = storageRef;
        this.uid = uid;
        this.callback = callback;
        this.db = db;
        this.finishCallback = finishCallback;

        if (recordTask == null) {
            recordTask = new RecordWaveTask(context);
        }
    }

    @Override
    public int getGroupCount() {
        return this.optionsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.optionsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.valuesList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_record_audio, parent, false);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        TextView numberText = (TextView) convertView.findViewById(R.id.record_audio_list_number);
        numberText.setText(String.valueOf(groupPosition + 1));

        ImageView tickImage = (ImageView) convertView.findViewById(R.id.record_audio_tick_img);

        if (statusList[groupPosition]) {
            tickImage.setVisibility(View.VISIBLE);
            numberText.setVisibility(View.GONE);
        } else {
            numberText.setVisibility(View.VISIBLE);
            tickImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String stageId = (String) getChild(groupPosition, childPosition);
        currGroupPos = groupPosition;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_record_audio, parent, false);
        }

        String intructionText = instrList.get(groupPosition);
        instructionText = (TextView) convertView.findViewById(R.id.text_record_instruction);
        instructionText.setText(intructionText);

        final String fileName = stageId + ".wav";

        //before record
        beforeRecordLyt = (LinearLayout) convertView.findViewById(R.id.layout_before_record);
        startRecordingBtn = (Button) convertView.findViewById(R.id.button_start_recording);
        playSampleBtn = (Button) convertView.findViewById(R.id.button_play_sample);

        //after record
        afterRecordLyt = (LinearLayout) convertView.findViewById(R.id.layout_after_record);
        playRecordingBtn = (Button) convertView.findViewById(R.id.button_play_recording);
        uploadRecordingBtn = (Button) convertView.findViewById(R.id.button_upload_recording);
        recordAgainBtn = (Button) convertView.findViewById(R.id.button_record_again);

        //upload record
        uploadRecordLyt = (LinearLayout) convertView.findViewById(R.id.layout_upload_record);
        Button cancelBtn = (Button) convertView.findViewById(R.id.button_cancel_upload);

        //finish recordings
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_recording_finish);

        if (groupPosition == valuesList.size() - 1) { //reached last position
            beforeRecordLyt.setVisibility(View.GONE);
            afterRecordLyt.setVisibility(View.GONE);
            uploadRecordLyt.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            finishCallback.handleDone();
            return convertView;
        }

        updateUI(state);

        startRecordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == STATE_NOT_STARTED) {
                    toggleButtonBg(startRecordingBtn, true);
                    launchTask(ABSOLUTE_PATH, fileName);
                    updateUI(STATE_RECORDING_START);
                } else if (state == STATE_RECORDING_START) {
                    toggleButtonBg(startRecordingBtn, false);
                    launchTask(ABSOLUTE_PATH, fileName);
                    updateUI(STATE_RECORDING_FINISH);
                }
            }
        });

        playSampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlaySample(!samplePlayStatus);
                exoPlayRawAudio(stageId, samplePlayStatus);
            }
        });

        playRecordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayRecording(!recordPlayStatus);
                exoPlayAudio(filePath, recordPlayStatus);
            }
        });

        recordAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete file
                File file = new File(filePath);
                if (file.exists()) {
                    boolean deleteStatus = file.delete();
                    Log.d(TAG, "recording deleted: " + deleteStatus);
                }
                reset();
            }
        });

        uploadRecordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filePath.isEmpty()) {
                    updateUI(STATE_UPLOADING);
                    uploadFile(filePath, stageId);
                } else {
                    Toast.makeText(context, "no file to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && !uploadTask.isComplete()) {
                    boolean cancelled = uploadTask.cancel();
                    Log.d(TAG, "upload cancelled: " + cancelled);
                    updateUI(STATE_RECORDING_FINISH);
                }
            }
        });

        return convertView;
    }

    private void toggleButtonBg(Button button, boolean flag) {
        if (flag) { //first click
            button.setBackground(context.getDrawable(R.drawable.rounded_shape_bg));
            button.setTextColor(context.getResources().getColor(R.color.white));
        } else { //revert back
            button.setBackground(context.getDrawable(R.drawable.rounded_border_bg));
            button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void togglePlaySample(boolean newStatus) {
        if (newStatus) { //clicked on "Play sample"
            playSampleBtn.setText(R.string.text_stop_playing);
            toggleButtonBg(playSampleBtn, true);
        } else { //clicked on "Stop"
            playSampleBtn.setText(R.string.text_play_sample);
            toggleButtonBg(playSampleBtn, false);
            samplePlayer.seekTo(0);
        }
        samplePlayStatus = newStatus;
    }

    private void togglePlayRecording(boolean newStatus) {
        if (newStatus) { //clicked on "Record"
            playRecordingBtn.setText(R.string.text_stop);
            uploadRecordingBtn.setVisibility(View.GONE);
            recordAgainBtn.setVisibility(View.GONE);
            toggleButtonBg(playRecordingBtn, true);
        } else { //clicked on "Stop recording"
            playRecordingBtn.setText(R.string.text_play);
            uploadRecordingBtn.setVisibility(View.VISIBLE);
            recordAgainBtn.setVisibility(View.VISIBLE);
            recordPlayer.seekTo(0);
            toggleButtonBg(playRecordingBtn, false);
        }
        recordPlayStatus = newStatus;
    }

    private int getRawFile(String type) {
        switch (type) {
            case "breathing-shallow":
                return R.raw.breathing_shallow;
            case "breathing-deep":
                return R.raw.breathing_deep;
            case "cough-shallow":
                return R.raw.cough_shallow;
            case "cough-heavy":
                return R.raw.cough_heavy;
            case "vowel-a":
                return R.raw.vowel_a;
            case "vowel-e":
                return R.raw.vowel_e;
            case "vowel-o":
                return R.raw.vowel_o;
            case "counting-normal":
                return R.raw.counting_normal;
            case "counting-fast":
                return R.raw.counting_fast;
            default:
                return 0;
        }
    }

    private void reset() {
        recordTask = new RecordWaveTask(context); //reset this async task as each instance can run only once

        if (recordPlayer != null) recordPlayer.release();
        recordPlayer = null;

        updateUI(STATE_NOT_STARTED);
    }

    private void uploadFile(String filePath, final String stageId) {
        Uri uri = Uri.fromFile(new File(filePath));
        String dateString = Utils.getDateString();
        StorageReference audioRef = storageRef
                .child("COLLECT_DATA")
                .child(dateString)
                .child(uid)
                .child(uri.getLastPathSegment());

        uploadTask = audioRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Upload failed. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reset(); //reset state
                statusList[currGroupPos] = true;

                if (samplePlayer != null) samplePlayer.release();
                samplePlayer = null;

                if (recordPlayer != null) recordPlayer.release();
                recordPlayer = null;

                HashMap<String, Object> updateAppData = new HashMap<>();
                updateAppData.put("cS", stageId);
                updateAppData.put("cSD", Utils.getISODate());
                updateAppData.put("lV", BuildConfig.VERSION_CODE);

                db.collection("USER_APPDATA")
                        .document(uid)
                        .update(updateAppData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "user app data upload success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "user app data upload failed: ", e);
                            }
                        });

                callback.expandNextGroup(currGroupPos);
            }
        });
    }

    private void exoPlayRawAudio(String type, boolean play) {
        if (samplePlayer == null) {
            Uri uri = RawResourceDataSource.buildRawResourceUri(getRawFile(type));

            DataSpec dataSpec = new DataSpec(uri);
            final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
            try {
                rawResourceDataSource.open(dataSpec);
            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }

            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };

            MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(rawResourceDataSource.getUri());

            samplePlayer = new SimpleExoPlayer.Builder(context).build();
            samplePlayer.prepare(mediaSource);

            samplePlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        togglePlaySample(false);
                        samplePlayer.setPlayWhenReady(false);
                    }
                }
            });
        }

        samplePlayer.setPlayWhenReady(play);
    }

    private void exoPlayAudio(String filePath, boolean play) {
        if (recordPlayer == null) {
            Uri uri = Uri.fromFile(new File(filePath));
            DataSpec dataSpec = new DataSpec(uri);
            final FileDataSource fileDataSource = new FileDataSource();
            try {
                fileDataSource.open(dataSpec);
            } catch (FileDataSource.FileDataSourceException e) {
                e.printStackTrace();
            }

            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return fileDataSource;
                }
            };

            MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(fileDataSource.getUri());

            recordPlayer = new SimpleExoPlayer.Builder(context).build();
            recordPlayer.prepare(mediaSource);

            recordPlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        togglePlayRecording(false);
                        recordPlayer.setPlayWhenReady(false);
                    }
                }
            });
        }

        recordPlayer.setPlayWhenReady(play);
    }

    private void launchTask(String path, String fileName) {
        if (state == STATE_NOT_STARTED) { //start recording
            File wavFile = new File(path, fileName);
            filePath = wavFile.getAbsolutePath();
            recordTask.execute(wavFile);
        } else if (state == STATE_RECORDING_START) { //stop recording
            if (recordTask != null && recordTask.getStatus() == AsyncTask.Status.RUNNING) {
                recordTask.cancel(true);
            }
        }
    }

    private void updateUI(int newState) {
        state = newState;

        switch (state) {
            case STATE_NOT_STARTED:
                beforeRecordLyt.setVisibility(View.VISIBLE);
                afterRecordLyt.setVisibility(View.GONE);
                uploadRecordLyt.setVisibility(View.GONE);
                playSampleBtn.setVisibility(View.VISIBLE);
                instructionText.setVisibility(View.VISIBLE);
                startRecordingBtn.setText(R.string.text_start_recording);
                break;
            case STATE_RECORDING_START:
                beforeRecordLyt.setVisibility(View.VISIBLE);
                afterRecordLyt.setVisibility(View.GONE);
                uploadRecordLyt.setVisibility(View.GONE);
                playSampleBtn.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                startRecordingBtn.setText(R.string.text_stop_recording);
                break;
            case STATE_RECORDING_FINISH:
                beforeRecordLyt.setVisibility(View.GONE);
                afterRecordLyt.setVisibility(View.VISIBLE);
                uploadRecordLyt.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                break;
            case STATE_UPLOADING:
                beforeRecordLyt.setVisibility(View.GONE);
                afterRecordLyt.setVisibility(View.GONE);
                uploadRecordLyt.setVisibility(View.VISIBLE);
                instructionText.setVisibility(View.GONE);
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
