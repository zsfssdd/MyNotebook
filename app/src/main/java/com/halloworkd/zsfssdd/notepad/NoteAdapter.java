package com.halloworkd.zsfssdd.notepad;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zsfss on 2017/3/11.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<NotePreview> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView title;
        TextView content;
        TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            title = (TextView) itemView.findViewById(R.id.note_title_1);
            content = (TextView) itemView.findViewById(R.id.note_content_1);
        }
    }

    public NoteAdapter(List<NotePreview> NoteList){
        mNoteList = NoteList;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                NotePreview note = mNoteList.get(position);
                Intent intent = new Intent(context, SNoteToolbarActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        NotePreview notePreview = mNoteList.get(position);
        holder.title.setText(notePreview.getTitle());
        holder.content.setText(notePreview.getContent());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
