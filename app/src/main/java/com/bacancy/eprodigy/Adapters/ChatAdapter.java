package com.bacancy.eprodigy.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bacancy.eprodigy.Activity.ChatContactDetailActivity;
import com.bacancy.eprodigy.Adapters.viewholder.ChatHolderFrom;
import com.bacancy.eprodigy.Adapters.viewholder.ChatHolderTo;
import com.bacancy.eprodigy.Adapters.viewholder.ChatImageHolder;
import com.bacancy.eprodigy.Adapters.viewholder.ChatImageRecvHolder;
import com.bacancy.eprodigy.Adapters.viewholder.HeaderHolder;
import com.bacancy.eprodigy.Adapters.viewholder.LocationFromHolder;
import com.bacancy.eprodigy.Adapters.viewholder.LocationToHolder;
import com.bacancy.eprodigy.Adapters.viewholder.RecvAudioHolder;
import com.bacancy.eprodigy.Adapters.viewholder.RecvContactHolder;
import com.bacancy.eprodigy.Adapters.viewholder.SendAudioHolder;
import com.bacancy.eprodigy.Adapters.viewholder.SendContactHolder;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.callback.ActorDiffCallback;
import com.bacancy.eprodigy.custom.StickyHeaderAdapter;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.SCUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by bacancy on 22/2/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<HeaderHolder> {
    int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3, MY_CONTACT = 4, OTHER_CONTACT = 5, MY_LOCATION = 6, OTHER_LOCATION = 7, MY_AUDIO = 8, OTHER_AUDIO = 9;
    private Context context;
    private List<ChatPojo> mLists;
    private SparseBooleanArray mSelectedItemsIds;
    private static final int HEADER_MESSAGE = 2;
    //private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, HEADER_MESSAGE = 2, IMAGE_OUTGOING = 3, CONTACT_OUTGOING = 4;
    private String mUsername;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
    private SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy");
    private SimpleDateFormat headerdateformat = new SimpleDateFormat("dd MMMM yyyy");
    private Date date = null;
    private String msgDate = "";
    private Date headerDate = null;
    private String msgHeader = "";
    private String msg = "";
    private String mName = "";
    private int day = 0;

    //set up MediaPlayer
    MediaPlayer audio = new MediaPlayer();

    public ChatAdapter(Context context, ArrayList<ChatPojo> conversation_arrayList, String username) {
        this.context = context;
        this.mLists = conversation_arrayList;
        mSelectedItemsIds = new SparseBooleanArray();
        this.mUsername = username;
    }

    public List<ChatPojo> getList() {
        return mLists;
    }


    public void swapItems(List<ChatPojo> actors) {
        final ActorDiffCallback diffCallback = new ActorDiffCallback(this.mLists, actors);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mLists.clear();
        this.mLists.addAll(actors);
        diffResult.dispatchUpdatesTo(this);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == MY_MESSAGE) {
            return new ChatHolderFrom(LayoutInflater.from(context).inflate(R.layout.my_message, viewGroup, false));
        } else if (i == OTHER_MESSAGE) {
            return new ChatHolderFrom(LayoutInflater.from(context).inflate(R.layout.their_message, viewGroup, false));
        } else if (i == MY_IMAGE) {
            return new ChatImageHolder(LayoutInflater.from(context).inflate(R.layout.outgoing_imageview, viewGroup, false));
        } else if (i == OTHER_IMAGE) {
            return new ChatImageRecvHolder(LayoutInflater.from(context).inflate(R.layout.incoming_imageview, viewGroup, false));
        } else if (i == MY_CONTACT) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.outgoing_contact, viewGroup, false);
            return new SendContactHolder(layoutView);
        } else if (i == OTHER_CONTACT) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.incoming_contact, viewGroup, false);
            return new RecvContactHolder(layoutView);
        } else if (i == MY_AUDIO) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.outgoing_audio, viewGroup, false);
            return new SendAudioHolder(layoutView);
        } else if(i == OTHER_AUDIO){
            View layoutView = LayoutInflater.from(context).inflate(R.layout.incoming_audio, viewGroup, false);
            return new RecvAudioHolder(layoutView);
        } else if (i == MY_LOCATION) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.outgoing_location, viewGroup, false);
            return new LocationFromHolder(layoutView);
        } else if (i == OTHER_LOCATION) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.incoming_location, viewGroup, false);
            return new LocationToHolder(layoutView);
        } else if (i == HEADER_MESSAGE) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.row_date_header, viewGroup, false);
            return new HeaderHolder(layoutView);
        } else {
            //default
            return new ChatHolderTo(LayoutInflater.from(context).inflate(R.layout.my_message, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ChatPojo chatPojo = mLists.get(position);

        String formatted_date = "";


        if (chatPojo.isMine()) {
            formatted_date = SCUtils.formatted_date((TextUtils.isEmpty(chatPojo.getChatTimestamp())) ? "" : chatPojo.getChatTimestamp());
        } else {
            String current_time_stamp = SCUtils.getCurrentTimeStamp();
            formatted_date = SCUtils.formatted_date((TextUtils.isEmpty(current_time_stamp)) ? "" : current_time_stamp);
        }


        if (holder instanceof ChatHolderFrom) {

            if (!TextUtils.isEmpty(chatPojo.getChatText())) {


                ((ChatHolderFrom) holder).tvMessage.setText(chatPojo.getChatText());


                ((ChatHolderFrom) holder).tvTime.setText(formatted_date);
            }

//            String userPic = Pref.getValue(context, AppConfing.USER_PROFILE_PIC,"");
//            if (!userPic.equals("")) {
//                Glide.with(context).load(userPic).apply(RequestOptions.circleCropTransform())
//                        .into(((ChatHolderFrom) holder).imgSenderPic);
//            }

        } else if (holder instanceof ChatHolderTo) {
            if (!TextUtils.isEmpty(chatPojo.getChatText())) {
                ((ChatHolderTo) holder).tvMessage.setText(chatPojo.getChatText());

                ((ChatHolderTo) holder).tvTime.setText(formatted_date);
            }

        } else if (holder instanceof ChatImageHolder) {
            if (!TextUtils.isEmpty(chatPojo.getChatImage())) {


                ((ChatImageHolder) holder).tvTime.setText(formatted_date);

                File f = new File(chatPojo.getChatImage());
                Picasso.with(context).load(f)
                        .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                        .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                        .into(((ChatImageHolder) holder).img_outgoing);
            }
        } else if (holder instanceof ChatImageRecvHolder) {
            if (!TextUtils.isEmpty(chatPojo.getChatImage())) {


                ((ChatImageRecvHolder) holder).tvTime.setText(formatted_date);

                Picasso.with(context).load(chatPojo.getChatText())
                        .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                        .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                        .into(((ChatImageRecvHolder) holder).img_incoming);
            }
        } else if (holder instanceof SendContactHolder) {

            String name = chatPojo.getSharedContactSenderName();
            String cono = chatPojo.getSharedContactSenderNumber();
            //  String image=chatPojo.getSharedContactSenderImage();

            if (!TextUtils.isEmpty(chatPojo.getSharedContactSenderName())
                    && !TextUtils.isEmpty(chatPojo.getSharedContactSenderNumber())) {

                ((SendContactHolder) holder).tv_time_outgoing.setText(formatted_date);
                  /*  Picasso.with(context).load(image)
                            .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .into(((SendContactHolder) holder).img_contact_outgoing);*/

                ((SendContactHolder) holder).tv_contact_name_outgoing.setText(name);
                ((SendContactHolder) holder).tv_phone_outgoing.setText(cono);
                ((SendContactHolder) holder).rl_contact_outgoing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatContactDetailActivity.StartChatContactDetailActivity(context, chatPojo);
                    }
                });
            }

        } else if (holder instanceof RecvContactHolder) {

            String name = chatPojo.getSharedContactSenderName();
            String cono = chatPojo.getSharedContactSenderNumber();
            // String image=chatPojo.getSharedContactRecvImage();

            if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(cono)) {

                ((RecvContactHolder) holder).tv_time_incoming.setText(formatted_date);
                   /* Picasso.with(context).load(image)
                            .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .into(((RecvContactHolder) holder).img_contact_incoming);*/

                ((RecvContactHolder) holder).tv_contact_name_incoming.setText(name);
                ((RecvContactHolder) holder).tv_phone_incoming.setText(cono);
                ((RecvContactHolder) holder).rl_contact_incoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatContactDetailActivity.StartChatContactDetailActivity(context, chatPojo);
                    }
                });

            }
        } else if (holder instanceof SendAudioHolder) {

            final String audioPath = chatPojo.getSendAudioPath();
            ((SendAudioHolder) holder).img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SendAudioHolder) holder).img_pause.setVisibility(View.VISIBLE);
                    ((SendAudioHolder) holder).img_play.setVisibility(View.GONE);
                    audioPlayer(audioPath,holder);
                }
            });

            ((SendAudioHolder) holder).img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SendAudioHolder) holder).img_pause.setVisibility(View.GONE);
                    ((SendAudioHolder) holder).img_play.setVisibility(View.VISIBLE);
                    audio.pause();
                }
            });

        } else if (holder instanceof RecvAudioHolder) {

            final String audioPath = chatPojo.getSendAudioPath();
            ((RecvAudioHolder) holder).img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RecvAudioHolder) holder).img_pause.setVisibility(View.VISIBLE);
                    ((RecvAudioHolder) holder).img_play.setVisibility(View.GONE);
                    audioPlayer(audioPath,holder);
                }
            });

            ((RecvAudioHolder) holder).img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RecvAudioHolder) holder).img_pause.setVisibility(View.GONE);
                    ((RecvAudioHolder) holder).img_play.setVisibility(View.VISIBLE);
                    audio.pause();
                }
            });

        } else if (holder instanceof LocationFromHolder) {

            String title = chatPojo.getLocationAddressTitle();
            String desc = chatPojo.getLocationAddressDesc();
            String strLatitude = chatPojo.getLocationAddressLatitude();
            String strLongitude = chatPojo.getLocationAddressLongitude();

            //  String image=chatPojo.getSharedContactSenderImage();

            if (!TextUtils.isEmpty(desc)
                    && !TextUtils.isEmpty(strLatitude)
                    && !TextUtils.isEmpty(strLongitude)
                    ) {

                ((LocationFromHolder) holder).tv_time_location_outgoing.setText(formatted_date);
                  /*  Picasso.with(context).load(image)
                            .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .into(((LocationFromHolder) holder).img_contact_outgoing);*/



                /*googleMapView.setMapType(MapType.SATELLITE);
                googleMapView.setMapScale(MapScale.HIGH);
                googleMapView.setMapZoom(15);
                googleMapView.setMapWidth(350);
                googleMapView.setMapHeight(350);
                googleMapView.setLocation(location);
                googleMapView.setZoomable(activity);*/



                if (TextUtils.isEmpty(title)) {
                    ((LocationFromHolder) holder).tv_title_location_outgoing.setVisibility(View.GONE);
                } else {
                    ((LocationFromHolder) holder).tv_title_location_outgoing.setVisibility(View.VISIBLE);
                    ((LocationFromHolder) holder).tv_title_location_outgoing.setText(title);
                }


                ((LocationFromHolder) holder).tv_desc_location_outgoing.setText(desc);


                ((LocationFromHolder) holder).rl_location_outgoing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Location clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } else if (holder instanceof LocationToHolder) {

            String title = chatPojo.getLocationAddressTitle();
            String desc = chatPojo.getLocationAddressDesc();
            String strLatitude = chatPojo.getLocationAddressLatitude();
            String strLongitude = chatPojo.getLocationAddressLongitude();

            //  String image=chatPojo.getSharedContactSenderImage();

            if (!TextUtils.isEmpty(desc)
                    && !TextUtils.isEmpty(strLatitude)
                    && !TextUtils.isEmpty(strLongitude)
                    ) {

                ((LocationToHolder) holder).tv_time_location_incoming.setText(formatted_date);
                  /*  Picasso.with(context).load(image)
                            .placeholder(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .error(context.getResources().getDrawable(R.mipmap.profile_pic))
                            .into(((LocationToHolder) holder).img_contact_outgoing);*/




                if (TextUtils.isEmpty(title)) {
                    ((LocationToHolder) holder).tv_title_location_incoming.setVisibility(View.GONE);
                } else {
                    ((LocationToHolder) holder).tv_title_location_incoming.setVisibility(View.VISIBLE);
                    ((LocationToHolder) holder).tv_title_location_incoming.setText(title);
                }

                ((LocationToHolder) holder).tv_desc_location_incoming.setText(desc);
                ((LocationToHolder) holder).rl_location_incoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Location clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } else if (holder instanceof HeaderHolder) {

            ChatPojo bean = mLists.get(position);

            msgHeader = SCUtils.formatted_date_only(bean.getChatTimestamp());

            ((HeaderHolder) holder).headerDate.setText(msgHeader);

            if (show(msgHeader) == 0) {
                ((HeaderHolder) holder).headerDate.setText("TODAY");
            } else if (show(msgHeader) == -1) {
                ((HeaderHolder) holder).headerDate.setText("YESTERDAY");
            } else {
                ((HeaderHolder) holder).headerDate.setText(msgHeader);
            }
        } else {

        }


//        holder.tvTime.setText(Html.fromHtml("<font color=\"#999999\">" + formatted_date + "</font>"));
        /** Change background color of the selected items in list view  **/
//        holder.itemView
//                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
//                        : Color.TRANSPARENT);

    }

    public void audioPlayer(String path, final RecyclerView.ViewHolder holder) {

        try {
            audio.setDataSource(path);
            audio.prepare();
            audio.start();

            audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    ((SendAudioHolder) holder).img_pause.setVisibility(View.GONE);
                    ((SendAudioHolder) holder).img_play.setVisibility(View.VISIBLE);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mLists.size();
    }


    public ChatPojo getItem(int firstVisiblePosition) {
        return mLists.get(firstVisiblePosition);
    }

    @Override
    public int getItemViewType(int position) {

        ChatPojo item = mLists.get(position);
        int mType = item.getMsgType();
        boolean isMy = item.isMine();

        if (mType == Constants.TYPE_MESSAGE && isMy) {
            return MY_MESSAGE;
        } else if (mType == Constants.TYPE_MESSAGE && !isMy) {
            return OTHER_MESSAGE;
        } else if (mType == Constants.TYPE_CONTACT && isMy) {
            return MY_CONTACT;
        } else if (mType == Constants.TYPE_CONTACT && !isMy) {
            return OTHER_CONTACT;
        } else if (mType == Constants.TYPE_IMAGE && isMy) {
            return MY_IMAGE;
        } else if (mType == Constants.TYPE_IMAGE && !isMy) {
            return OTHER_IMAGE;
        } else if (mType == Constants.TYPE_LOCATION && isMy) {
            return MY_LOCATION;
        } else if (mType == Constants.TYPE_LOCATION && !isMy) {
            return OTHER_LOCATION;
        } else if (mType == Constants.TYPE_AUDIO && isMy) {
            return MY_AUDIO;
        } else if (mType == Constants.TYPE_AUDIO && !isMy) {
            return OTHER_AUDIO;
        } else {
            return 100;
        }
    }

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or deleteTbl selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    @Override
    public long getHeaderId(int position) {
        String mDate = mLists.get(position).getChatTimestamp();
        return SCUtils.getDatetoLong(SCUtils.formatted_date_only(mDate));
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {

        return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.row_date_header, viewGroup, false));

    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int position) {

        //set header text as first char in name
        ChatPojo bean = mLists.get(position);

        msgHeader = SCUtils.formatted_date_only(bean.getChatTimestamp());

        holder.headerDate.setText(msgHeader);

        if (show(msgHeader) == 0) {
            holder.headerDate.setText("TODAY");
        } else if (show(msgHeader) == -1) {
            holder.headerDate.setText("YESTERDAY");
        } else {
            holder.headerDate.setText(msgHeader);
        }
    }

    private int show(String time) {
        try {
            String outputPattern = "dd MMMM yyyy";
            SimpleDateFormat format = new SimpleDateFormat(outputPattern);

            Date Date1 = format.parse(getdate());
            Date Date2 = format.parse(time);
            long mills = Date2.getTime() - Date1.getTime();
            long Day1 = mills / (1000 * 60 * 60);

            day = (int) Day1 / 24;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }


    private String getdate() {
        String time = "";
        try {
            String outputPattern = "dd MMMM yyyy";

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(outputPattern);
            time = df.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


}
