package com.bacancy.eprodigy.Adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bacancy.eprodigy.API.ActorDiffCallback;
import com.bacancy.eprodigy.Adapters.viewholder.ChatHolderFrom;
import com.bacancy.eprodigy.Adapters.viewholder.ChatHolderTo;
import com.bacancy.eprodigy.Adapters.viewholder.ChatImageHolder;
import com.bacancy.eprodigy.Adapters.viewholder.HeaderHolder;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.custom.StickyHeaderAdapter;
import com.bacancy.eprodigy.utils.SCUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by samir on 22/2/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private Context context;
    private List<ChatPojo> mLists;
    private SparseBooleanArray mSelectedItemsIds;
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, HEADER_MESSAGE = 2, IMAGE_OUTGOING = 3;
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
            return new ChatHolderFrom(LayoutInflater.from(context).inflate(R.layout.their_message, viewGroup, false));
        } else if(i == IMAGE_OUTGOING){
            return new ChatImageHolder(LayoutInflater.from(context).inflate(R.layout.outgoing_imageview,viewGroup,false));
        } else if (i == HEADER_MESSAGE) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.row_date_header, viewGroup, false);
            return new HeaderHolder(layoutView);
        } else {
            return new ChatHolderTo(LayoutInflater.from(context).inflate(R.layout.my_message, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ChatPojo chatPojo = mLists.get(position);

        if (holder instanceof ChatHolderFrom) {

            ((ChatHolderFrom) holder).tvMessage.setText(chatPojo.getChatText());
            String formatted_date = SCUtils.formatted_date(chatPojo.getChatTimestamp());
            ((ChatHolderFrom) holder).tvTime.setText(formatted_date);

//            String userPic = Pref.getValue(context, AppConfing.USER_PROFILE_PIC,"");
//            if (!userPic.equals("")) {
//                Glide.with(context).load(userPic).apply(RequestOptions.circleCropTransform())
//                        .into(((ChatHolderFrom) holder).imgSenderPic);
//            }

        } else if (holder instanceof ChatHolderTo) {
            ((ChatHolderTo) holder).tvMessage.setText(chatPojo.getChatText());
            String formatted_date = SCUtils.formatted_date(chatPojo.getChatTimestamp());
            ((ChatHolderTo) holder).tvTime.setText(formatted_date);

        } else if (holder instanceof ChatImageHolder){
            Picasso.with(context).load(chatPojo.getChatImage()).into(((ChatImageHolder) holder).img_outgoing);
        }else if(holder instanceof HeaderHolder){

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
        }else {

        }


//        holder.tvTime.setText(Html.fromHtml("<font color=\"#999999\">" + formatted_date + "</font>"));
        /** Change background color of the selected items in list view  **/
//        holder.itemView
//                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
//                        : Color.TRANSPARENT);

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

        if (item.getChatSender().equalsIgnoreCase(mUsername))
            return MY_MESSAGE;
        else if (!item.getChatImage().equals("") && !item.getChatImage().isEmpty()){
            return IMAGE_OUTGOING;
        }
        else return OTHER_MESSAGE;
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
