package com.example.a201912098_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    BlockButton[][] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        ToggleButton mod = (ToggleButton) findViewById(R.id.toggleButton);
        TextView remainMines = (TextView) findViewById(R.id.remainMines);

        buttons = initBoard(table, mod, remainMines);
        mineGenerate(buttons);
        mineSearch(buttons);
    }

    public BlockButton[][] initBoard(TableLayout table, ToggleButton mod, TextView remainMines) {
        BlockButton[][] button = new BlockButton[9][9];
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                button[i][j] = new BlockButton(this, i, j);
                TableRow.LayoutParams layoutParams =
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                1.0f);
                button[i][j].setLayoutParams(layoutParams);
                tableRow.addView(button[i][j]);
                button[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BlockButton btn = (BlockButton) view;
                        if(mod.isChecked()) {   //flag 모드
                            btn.toggleFlag();
                            remainMines.setText("" + btn.getFlags());
                        }
                        else {   //break 모드
                            if(!btn.isFlag()) {
                                if(!breakBlock(btn)) {
                                    GaveOver(btn, false);
                                }
                            }
                        }
                    }
                });
            }
        }
        return button;
    }

    public boolean breakBlock(View view) {
        BlockButton button = ((BlockButton) view);

        int x = button.get_X();
        int y = button.get_Y();
        blockSearch(buttons[x][y]);
        System.out.println("메소드 실행됨!" + button.get_X() + button.get_Y());
        if(button.isFlag()) {
            button.toggleFlag();
            TextView remainMines = (TextView) findViewById(R.id.remainMines);
            remainMines.setText("" + button.getFlags());
        }

        if(!button.isClickable()) {
            System.out.println("if button is not Clickable");
        }
        else if (button.breakBlock()) {
            System.out.println("지뢰 탐지!");
            return false;
            //GaveOver(button, false);
        }
        else {
            System.out.println("블록 개수1 :" + (button.getBlocks() + button.getFlags()));
            if(button.getIsNeighborMines() == 0){
                System.out.println("else none mine");
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        int row = x + i;
                        int col = y + j;
                        if(i == 0 && j == 0)
                            continue;
                        else if((row >= 0 && row < 9) && (col >= 0 && col < 9))
                            breakBlock(button);
                    }
                }
            }
            if(button.getBlocks() == 0) {
                System.out.println("게임 클리어!");
                GaveOver(button, true);
            }
        }
        return true;
    }

    public void mineGenerate(BlockButton[][] button) {
        Random random = new Random();
        boolean[][] mine = new boolean[9][9]; // 중복 여부를 체크하는 배열
        for (int i = 0; i < 10; i++) {
            int x, y;
            do {
                x = random.nextInt(9);
                y = random.nextInt(9);
            } while (mine[x][y]); // 이미 선택한 인덱스일 경우 다시 선택
            mine[x][y] = true;
            button[x][y].setMine(true);
            //button[x][y].setText("ㅈ"); //지뢰 위치 확인
        }
    }

    public void mineSearch(BlockButton[][] button) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                int mineCount = 0;
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        int row = x + i;
                        int col = y + j;
                        if(i == 0 && j == 0)
                            continue;
                        else if((row >= 0 && row < 9) && (col >= 0 && col < 9))
                            if(button[row][col].isMine())
                                ++mineCount;
                        else
                            continue;
                    }
                }
                button[x][y].setIsNeighborMines(mineCount);
            }
        }
    }

    public void blockSearch(BlockButton button) {
        int x = button.get_X();
        int y = button.get_Y();
        System.out.println("메소드 실행됨!" + x + y);
        if(button.isFlag()) {
            button.toggleFlag();
            TextView remainMines = (TextView) findViewById(R.id.remainMines);
            remainMines.setText("" + button.getFlags());
        }

        if(!button.isClickable()) {
            System.out.println("if button is not Clickable");
        }
        else if (button.breakBlock()) {
            System.out.println("지뢰 탐지!");
            GaveOver(button, false);
        }
        else {
            System.out.println("블록 개수1 :" + (button.getBlocks() + button.getFlags()));
            if(button.getIsNeighborMines() == 0){
                System.out.println("else none mine");
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        int row = x + i;
                        int col = y + j;
                        if(i == 0 && j == 0)
                            continue;
                        else if((row >= 0 && row < 9) && (col >= 0 && col < 9))
                            blockSearch(buttons[row][col]);
                    }
                }
            }
            if(button.getBlocks() == 0) {
                System.out.println("게임 클리어!");
                GaveOver(button, true);
            }
        }
    }

    public void GaveOver(BlockButton button, boolean win) {
        if(win) {
            Toast.makeText(MainActivity.this, "you win!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "game over!", Toast.LENGTH_SHORT).show();
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    if(buttons[i][j].isMine())
                        buttons[i][j].setText("*");
                    buttons[i][j].setClickable(false);
                }
            }
        }
    }

}