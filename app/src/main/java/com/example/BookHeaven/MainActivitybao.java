package com.example.BookHeaven;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.BookHeaven.models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivitybao extends AppCompatActivity {
    private static final String TAG = "MainActivitybao";
    private TextView contentText;
    private TextView chapterTitleText;
    private ScrollView scrollView;
    private LinearLayout bookmarkPanel;
    private LinearLayout savedPositionsPanel;
    private LinearLayout customizePanel;
    private ListView chapterListView;
    private ListView bookmarkListView;
    private ImageView bookCover;
    private ImageView bookCoverSaved;
    private TextView bookTitle;
    private TextView bookTitleSaved;
    private float currentTextSize = 16;
    private int currentMargin = 0;
    private int currentChapter = 1;
    private int totalChapters = 0;
    private SharedPreferences preferences;
    private boolean isBookmarkPanelVisible = false;
    private boolean isSavedPositionsPanelVisible = false;
    private boolean isCustomizePanelVisible = false;
    private String bookTitleFromIntent = "Kinh tế số";
    private int currentBackgroundColor = 0xFFFFFFFF; // Màu trắng mặc định

    private List<String> chapterTitlesList = new ArrayList<>();
    private List<String> chapterContentsList = new ArrayList<>();
    private String coverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bao);

        // Nhận dữ liệu từ Intent
        Book book = (Book) getIntent().getSerializableExtra("book");
        if (book == null) {
            Toast.makeText(this, "Không nhận được dữ liệu sách!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bookTitleFromIntent = book.getTitle();
        preferences = getSharedPreferences("EbookReader_" + bookTitleFromIntent, Context.MODE_PRIVATE);

        contentText = findViewById(R.id.contentText);
        chapterTitleText = findViewById(R.id.chapterTitleText);
        scrollView = findViewById(R.id.scrollView);
        bookmarkPanel = findViewById(R.id.bookmarkPanel);
        savedPositionsPanel = findViewById(R.id.savedPositionsPanel);
        customizePanel = findViewById(R.id.customizePanel);
        chapterListView = findViewById(R.id.chapterListView);
        bookmarkListView = findViewById(R.id.bookmarkListView);
        bookCover = findViewById(R.id.bookCover);
        bookCoverSaved = findViewById(R.id.bookCoverSaved);
        bookTitle = findViewById(R.id.bookTitle);
        bookTitleSaved = findViewById(R.id.bookTitleSaved);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton prevChapterButton = findViewById(R.id.prevChapterButton);
        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton textSizeButton = findViewById(R.id.textSizeButton);
        ImageButton bookmarkButton = findViewById(R.id.bookmarkButton);
        ImageButton savePositionButton = findViewById(R.id.savePositionButton);
        ImageButton savePositionButtonBottom = findViewById(R.id.savePositionButtonBottom);
        ImageButton nextChapterButton = findViewById(R.id.nextChapterButton);

        View colorBlack = customizePanel.findViewById(R.id.colorBlack);
        View colorGreen = customizePanel.findViewById(R.id.colorGreen);
        View colorYellow = customizePanel.findViewById(R.id.colorYellow);
        View colorPink = customizePanel.findViewById(R.id.colorPink);
        View colorBlue = customizePanel.findViewById(R.id.colorBlue);
        Spinner fontSpinner = customizePanel.findViewById(R.id.fontSpinner);
        ImageButton decreaseFontSizeButton = customizePanel.findViewById(R.id.decreaseFontSizeButton);
        TextView fontSizeText = customizePanel.findViewById(R.id.fontSizeText);
        ImageButton increaseFontSizeButton = customizePanel.findViewById(R.id.increaseFontSizeButton);
        ImageButton decreaseMarginButton = customizePanel.findViewById(R.id.decreaseMarginButton);
        TextView marginText = customizePanel.findViewById(R.id.marginText);
        ImageButton increaseMarginButton = customizePanel.findViewById(R.id.increaseMarginButton);

        currentChapter = preferences.getInt("currentChapter", 1);
        final int savedScrollPosition = preferences.getInt("scrollPosition", 0);

        // Lấy thông tin từ Book
        coverUrl = book.getImageUrl();
        if (coverUrl != null && !coverUrl.isEmpty()) {
            Picasso.get().load(coverUrl).into(bookCover);
            Picasso.get().load(coverUrl).into(bookCoverSaved);
        } else {
            bookCover.setImageResource(R.drawable.book_khoi_nghiep);
            bookCoverSaved.setImageResource(R.drawable.book_khoi_nghiep);
        }

        bookTitle.setText(bookTitleFromIntent);
        bookTitleSaved.setText(bookTitleFromIntent);

        // Lấy chapters từ Book
        chapterTitlesList.clear();
        chapterContentsList.clear();
        List<Book.Chapter> chapters = book.getChapters();
        if (chapters != null) {
            for (Book.Chapter chapter : chapters) {
                if (chapter != null) { // Kiểm tra chapter không null
                    String chapterTitle = chapter.getTitle();
                    String chapterContent = chapter.getContent();
                    if (chapterTitle != null && chapterContent != null) {
                        chapterTitlesList.add(chapterTitle);
                        chapterContentsList.add(chapterContent);
                    }
                }
            }
        }

        totalChapters = chapterTitlesList.size();
        if (totalChapters == 0) {
            // Thêm chương mặc định nếu không có chapters
            chapterTitlesList.add("Chương 1");
            chapterContentsList.add("Không có nội dung chương nào được cung cấp.");
            totalChapters = 1;
        }

        ChapterAdapter adapter = new ChapterAdapter(MainActivitybao.this, chapterTitlesList.toArray(new String[0]));
        chapterListView.setAdapter(adapter);

        if (currentChapter > totalChapters) {
            currentChapter = 1;
        }

        updateChapterContent();
        scrollView.post(() -> scrollView.scrollTo(0, savedScrollPosition));

        String[] fonts = {"Times New Roman", "Arial", "Roboto"};    
        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fonts);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFont = fonts[position];
                Typeface typeface;
                switch (selectedFont) {
                    case "Times New Roman":
                        typeface = Typeface.SERIF;
                        break;
                    case "Arial":
                        typeface = Typeface.SANS_SERIF;
                        break;
                    case "Roboto":
                        typeface = Typeface.create("sans-serif", Typeface.NORMAL);
                        break;
                    default:
                        typeface = Typeface.DEFAULT;
                        break;
                }
                contentText.setTypeface(typeface);
                chapterTitleText.setTypeface(typeface);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        scrollView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isBookmarkPanelVisible) toggleBookmarkPanel();
                if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
                if (isCustomizePanelVisible) toggleCustomizePanel();
            }
            return false;
        });

        backButton.setOnClickListener(v -> {
            if (isBookmarkPanelVisible) toggleBookmarkPanel();
            else if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            else if (isCustomizePanelVisible) toggleCustomizePanel();
            else finish();
        });

        savePositionButton.setOnClickListener(v -> {
            int scrollPosition = scrollView.getScrollY();
            addBookmark(currentChapter, scrollPosition);
            Toast.makeText(MainActivitybao.this, "Đã lưu vị trí đọc", Toast.LENGTH_SHORT).show();
        });

        savePositionButtonBottom.setOnClickListener(v -> {
            if (isCustomizePanelVisible) toggleCustomizePanel();
            if (isBookmarkPanelVisible) toggleBookmarkPanel();
            if (!isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            List<Bookmark> bookmarks = getBookmarks();
            BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(MainActivitybao.this, bookmarks);
            bookmarkListView.setAdapter(bookmarkAdapter);
            Toast.makeText(MainActivitybao.this, "Danh sách vị trí đã lưu", Toast.LENGTH_SHORT).show();
        });

        textSizeButton.setOnClickListener(v -> {
            if (isBookmarkPanelVisible) toggleBookmarkPanel();
            if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            toggleCustomizePanel();
        });

        prevChapterButton.setOnClickListener(v -> {
            if (isBookmarkPanelVisible) toggleBookmarkPanel();
            if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            if (isCustomizePanelVisible) toggleCustomizePanel();
            if (chapterTitlesList.isEmpty() || chapterContentsList.isEmpty()) {
                Toast.makeText(MainActivitybao.this, "Dữ liệu chương chưa tải xong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentChapter > 1) {
                currentChapter--;
                updateChapterContent();
                scrollView.scrollTo(0, 0);
                Toast.makeText(MainActivitybao.this, "Chương " + currentChapter, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivitybao.this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
            }
        });

        nextChapterButton.setOnClickListener(v -> {
            if (isBookmarkPanelVisible) toggleBookmarkPanel();
            if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            if (isCustomizePanelVisible) toggleCustomizePanel();
            if (chapterTitlesList.isEmpty() || chapterContentsList.isEmpty()) {
                Toast.makeText(MainActivitybao.this, "Dữ liệu chương chưa tải xong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentChapter < totalChapters) {
                currentChapter++;
                updateChapterContent();
                scrollView.scrollTo(0, 0);
                Toast.makeText(MainActivitybao.this, "Chương " + currentChapter, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivitybao.this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
            }
        });

        bookmarkButton.setOnClickListener(v -> {
            if (isCustomizePanelVisible) toggleCustomizePanel();
            if (isSavedPositionsPanelVisible) toggleSavedPositionsPanel();
            toggleBookmarkPanel();
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivitybao.this, TrangChu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        chapterListView.setOnItemClickListener((parent, view, position, id) -> {
            if (chapterTitlesList.isEmpty() || chapterContentsList.isEmpty()) {
                Toast.makeText(MainActivitybao.this, "Dữ liệu chương chưa tải xong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (position >= 0 && position < chapterTitlesList.size()) {
                currentChapter = position + 1;
                updateChapterContent();
                scrollView.scrollTo(0, 0);
                toggleBookmarkPanel();
                Toast.makeText(MainActivitybao.this, chapterTitlesList.get(position), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivitybao.this, "Chương không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        bookmarkListView.setOnItemClickListener((parent, view, position, id) -> {
            if (chapterTitlesList.isEmpty() || chapterContentsList.isEmpty()) {
                Toast.makeText(MainActivitybao.this, "Dữ liệu chương chưa tải xong", Toast.LENGTH_SHORT).show();
                return;
            }
            Bookmark bookmark = (Bookmark) parent.getItemAtPosition(position);
            currentChapter = bookmark.getChapter();
            final int scrollPosition = bookmark.getScrollPosition();
            if (currentChapter >= 1 && currentChapter <= totalChapters) {
                updateChapterContent();
                scrollView.post(() -> scrollView.scrollTo(0, scrollPosition));
                toggleSavedPositionsPanel();
                Toast.makeText(MainActivitybao.this, "Đã chuyển đến vị trí: Chương " + currentChapter, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivitybao.this, "Chương không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        colorBlack.setOnClickListener(v -> {
            if (currentBackgroundColor == 0xFF000000) { // Nếu đang là màu đen
                scrollView.setBackgroundColor(0xFFFFFFFF); // Trở về trắng
                contentText.setTextColor(0xFF000000); // Màu chữ đen
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFFF;
            } else {
                scrollView.setBackgroundColor(0xFF000000);
                contentText.setTextColor(0xFFFFFFFF); // Màu chữ trắng
                chapterTitleText.setTextColor(0xFFFFFFFF);
                currentBackgroundColor = 0xFF000000;
            }
        });

        colorGreen.setOnClickListener(v -> {
            if (currentBackgroundColor == 0xFF00FF00) { // Nếu đang là màu xanh lá
                scrollView.setBackgroundColor(0xFFFFFFFF);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFFF;
            } else {
                scrollView.setBackgroundColor(0xFF00FF00);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFF00FF00;
            }
        });

        colorYellow.setOnClickListener(v -> {
            if (currentBackgroundColor == 0xFFFFFFCC) { // Nếu đang là màu vàng
                scrollView.setBackgroundColor(0xFFFFFFFF);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFFF;
            } else {
                scrollView.setBackgroundColor(0xFFFFFFCC);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFCC;
            }
        });

        colorPink.setOnClickListener(v -> {
            if (currentBackgroundColor == 0xFFFF99CC) { // Nếu đang là màu hồng
                scrollView.setBackgroundColor(0xFFFFFFFF);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFFF;
            } else {
                scrollView.setBackgroundColor(0xFFFF99CC);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFF99CC;
            }
        });

        colorBlue.setOnClickListener(v -> {
            if (currentBackgroundColor == 0xFF99CCFF) { // Nếu đang là màu xanh dương
                scrollView.setBackgroundColor(0xFFFFFFFF);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFFFFFFFF;
            } else {
                scrollView.setBackgroundColor(0xFF99CCFF);
                contentText.setTextColor(0xFF000000);
                chapterTitleText.setTextColor(0xFF000000);
                currentBackgroundColor = 0xFF99CCFF;
            }
        });
        scrollView.setBackgroundColor(0xFFFFFFFF); // Màu trắng
        contentText.setTextColor(0xFF000000); // Màu chữ đen
        chapterTitleText.setTextColor(0xFF000000);
        currentBackgroundColor = 0xFFFFFFFF; // Khởi tạo màu hiện tại

        decreaseFontSizeButton.setOnClickListener(v -> {
            if (currentTextSize > 12) {
                currentTextSize -= 2;
                contentText.setTextSize(currentTextSize);
                fontSizeText.setText(currentTextSize + "px");
            }
        });

        increaseFontSizeButton.setOnClickListener(v -> {
            if (currentTextSize < 28) {
                currentTextSize += 2;
                contentText.setTextSize(currentTextSize);
                fontSizeText.setText(currentTextSize + "px");
            }
        });

        decreaseMarginButton.setOnClickListener(v -> {
            if (currentMargin > 0) {
                currentMargin -= 2;
                contentText.setPadding(currentMargin, 16, currentMargin, 16);
                marginText.setText(currentMargin + "px");
            }
        });

        increaseMarginButton.setOnClickListener(v -> {
            if (currentMargin < 32) {
                currentMargin += 2;
                contentText.setPadding(currentMargin, 16, currentMargin, 16);
                marginText.setText(currentMargin + "px");
            }
        });
    }

    private class ChapterAdapter extends ArrayAdapter<String> {
        public ChapterAdapter(Context context, String[] chapters) {
            super(context, 0, chapters);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bookmark_item, parent, false);
            }
            TextView chapterTitle = convertView.findViewById(R.id.chapterTitle);
            chapterTitle.setText(getItem(position));
            if (position == currentChapter - 1) {
                convertView.setBackgroundColor(0x22000000);
            } else {
                convertView.setBackgroundColor(0x00000000);
            }
            return convertView;
        }
    }

    private class BookmarkAdapter extends ArrayAdapter<Bookmark> {
        public BookmarkAdapter(Context context, List<Bookmark> bookmarks) {
            super(context, 0, bookmarks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bookmark_item, parent, false);
            }
            TextView bookmarkTitle = convertView.findViewById(R.id.chapterTitle);
            Bookmark bookmark = getItem(position);
            bookmarkTitle.setText("Chương " + bookmark.getChapter() + " - Vị trí: " + bookmark.getScrollPosition());
            return convertView;
        }
    }

    private List<Bookmark> getBookmarks() {
        String bookmarksJson = preferences.getString("bookmarks", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Bookmark>>() {}.getType();
        List<Bookmark> bookmarks = gson.fromJson(bookmarksJson, type);
        return bookmarks != null ? bookmarks : new ArrayList<>();
    }

    private void saveBookmarks(List<Bookmark> bookmarks) {
        Gson gson = new Gson();
        String bookmarksJson = gson.toJson(bookmarks);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bookmarks", bookmarksJson);
        editor.apply();
    }

    private void addBookmark(int chapter, int scrollPosition) {
        List<Bookmark> bookmarks = getBookmarks();
        bookmarks.add(new Bookmark(chapter, scrollPosition));
        saveBookmarks(bookmarks);
    }

    private void updateChapterContent() {
        if (chapterTitlesList.isEmpty() || chapterContentsList.isEmpty()) {
            Toast.makeText(MainActivitybao.this, "Dữ liệu chương chưa tải xong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentChapter < 1 || currentChapter > chapterTitlesList.size()) currentChapter = 1;
        chapterTitleText.setText(chapterTitlesList.get(currentChapter - 1));
        contentText.setText(chapterContentsList.get(currentChapter - 1));
    }

    private void saveReadingPosition() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentChapter", currentChapter);
        editor.putInt("scrollPosition", scrollView.getScrollY());
        editor.apply();
    }

    private void toggleBookmarkPanel() {
        isBookmarkPanelVisible = !isBookmarkPanelVisible;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int panelWidth = screenWidth / 2;
        ViewGroup.LayoutParams panelParams = bookmarkPanel.getLayoutParams();
        panelParams.width = panelWidth;
        bookmarkPanel.setLayoutParams(panelParams);
        bookmarkPanel.setVisibility(isBookmarkPanelVisible ? View.VISIBLE : View.GONE);
        if (isBookmarkPanelVisible && chapterListView.getAdapter() != null) {
            ((ChapterAdapter) chapterListView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void toggleSavedPositionsPanel() {
        isSavedPositionsPanelVisible = !isSavedPositionsPanelVisible;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int panelWidth = screenWidth / 2;
        ViewGroup.LayoutParams panelParams = savedPositionsPanel.getLayoutParams();
        panelParams.width = panelWidth;
        savedPositionsPanel.setLayoutParams(panelParams);
        savedPositionsPanel.setVisibility(isSavedPositionsPanelVisible ? View.VISIBLE : View.GONE);
    }

    private void toggleCustomizePanel() {
        isCustomizePanelVisible = !isCustomizePanelVisible;
        customizePanel.setVisibility(isCustomizePanelVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isBookmarkPanelVisible) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int panelWidth = screenWidth / 2;
            ViewGroup.LayoutParams panelParams = bookmarkPanel.getLayoutParams();
            panelParams.width = panelWidth;
            bookmarkPanel.setLayoutParams(panelParams);
        }
        if (isSavedPositionsPanelVisible) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int panelWidth = screenWidth / 2;
            ViewGroup.LayoutParams panelParams = savedPositionsPanel.getLayoutParams();
            panelParams.width = panelWidth;
            savedPositionsPanel.setLayoutParams(panelParams);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveReadingPosition();
    }
}