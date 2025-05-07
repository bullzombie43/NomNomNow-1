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

    private List<String> instructions;

    public InstructionAdapter(List<String> instructions) {
        this.instructions = instructions;
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
        return instructions.size();
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

            instructionEdit.setText(instructions.get(position));

            textWatcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    instructions.set(getAdapterPosition(), editable.toString());
                }

            };

            instructionEdit.addTextChangedListener(textWatcher);
        }
    }

    public List<String> getUpdatedInstructions() {
        return instructions;
    }
}
