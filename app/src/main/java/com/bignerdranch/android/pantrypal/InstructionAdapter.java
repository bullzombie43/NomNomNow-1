package com.bignerdranch.android.pantrypal;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {
    private Recipe mRecipe;

    public InstructionAdapter(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipe.getInstructions().size();
    }

    class InstructionViewHolder extends RecyclerView.ViewHolder {
        EditText instructionEdit;
        TextWatcher textWatcher;

        InstructionViewHolder(View itemView) {
            super(itemView);
            instructionEdit = itemView.findViewById(R.id.instruction_edit);
        }

        void bind(int position) {
            // Remove previous watcher if any to avoid weird bugs
            if (textWatcher != null) {
                instructionEdit.removeTextChangedListener(textWatcher);
            }

            instructionEdit.setText(mRecipe.getInstructions().get(position));

            textWatcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) return;

                    String newText = s.toString();
                    mRecipe.getInstructions().set(position, newText);

                    // If we're editing the last item and it's no longer blank, add a new blank step
                    if (position == mRecipe.getInstructions().size() - 1 && !newText.trim().isEmpty()) {
                        mRecipe.getInstructions().add("");  // Add blank item
                        notifyItemInserted(mRecipe.getInstructions().size() - 1);
                    }

                    // If we're editing the second-to-last item and the last item is now extra (blank), remove it
                    if (position == mRecipe.getInstructions().size() - 2 && newText.trim().isEmpty() &&
                            mRecipe.getInstructions().get(mRecipe.getInstructions().size() - 1).trim().isEmpty()) {
                        mRecipe.getInstructions().remove(mRecipe.getInstructions().size() - 1);
                        notifyItemRemoved(mRecipe.getInstructions().size());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION) return;

                    mRecipe.getInstructions().set(getAdapterPosition(), editable.toString());


                    // If we're editing the last item and it's no longer blank, add a new blank one
                    if (pos == mRecipe.getInstructions().size() - 1 && !editable.toString().isEmpty()) {
                        mRecipe.getInstructions().add("");
                        notifyItemInserted(mRecipe.getInstructions().size() - 1);
                    }
                }

            };

            instructionEdit.addTextChangedListener(textWatcher);
        }
    }

    public List<String> getUpdatedInstructions() {
        return mRecipe.getInstructions();
    }
}
