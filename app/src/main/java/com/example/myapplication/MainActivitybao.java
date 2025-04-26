package com.example.myapplication;

import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivitybao extends AppCompatActivity {
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
    private static final int TOTAL_CHAPTERS = 4;
    private SharedPreferences preferences;
    private boolean isBookmarkPanelVisible = false;
    private boolean isSavedPositionsPanelVisible = false;
    private boolean isCustomizePanelVisible = false;

    // Mảng tiêu đề các chương
    private String[] chapterTitles = {
            "Chương 01: Kinh tế",
            "Chương 02: Kinh doanh",
            "Chương 03: Quản lý",
            "Chương 04: Phát triển"
    };

    // Mảng nội dung các chương
    private String[] chapterContents = {
            "Chương 1: Cuốn sách mang đến cho bạn 4 ý tưởng sâu sắc mà nếu thấu hiểu được, " +
                    "bạn sẽ có được tiềm thức và sức mạnh để tạo dựng một doanh nghiệp nhỏ phát triển bền vững. " +
                    "Còn nếu bỏ qua, bạn sẽ giống như hàng nghìn người đầu tư công sức, tiền bạc và cả cuộc sống " +
                    "để khởi nghiệp nhưng vẫn thất bại, hay phải vật vã mãi để duy trì sự tồn tại nhạt nhoà cho " +
                    "doanh nghiệp của mình...",

            "Chương 2: \"Để xây dựng doanh nghiệp hiệu quả\", Gerber chỉ ra rằng thực tế là hầu hết các chủ " +
                    "doanh nghiệp nhỏ đều xuất phát từ các nhà chuyên môn: kỹ sư, lập trình viên, kế toán... họ làm " +
                    "tốt công việc chuyên môn, vì vậy họ tin rằng nếu thành lập doanh nghiệp riêng, họ sẽ có cơ hội " +
                    "tự do làm công việc yêu thích và kiếm được nhiều tiền hơn.",

            "Chương 3: Nhưng khi thành lập doanh nghiệp, các nhà chuyên môn thường có khuynh hướng chỉ tập " +
                    "làm những gì họ giỏi và phớt lờ các yếu tố quan trọng khác của kinh doanh. Thiếu mục tiêu nên " +
                    "quá tải, kiệt sức và cuối cùng phá sản. Thay vì sở hữu doanh nghiệp, họ chỉ sở hữu công việc.",

            "Chương 4: Thay vì, vai trò doanh nghiệp hoàn toàn khác: họ cần tạo dựng một doanh nghiệp hoạt " +
                    "động độc lập với bản thân. Chủ doanh nghiệp phải hình thành rõ sao, cần hoạch định các chiến " +
                    "lược nào về nhân sự, marketing, quản lý... Dần dần, chủ doanh nghiệp phải làm việc ít hơn và " +
                    "thiết lập hệ thống vận hành cho từng vị trí."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bao);

        // Khởi tạo SharedPreferences để lưu vị trí đọc
        preferences = getSharedPreferences("EbookReader", Context.MODE_PRIVATE);

        // Khởi tạo các view
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

        // Khởi tạo các view trong customize panel
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

        // Khôi phục chương hiện tại và vị trí cuộn
        currentChapter = preferences.getInt("currentChapter", 1);
        final int savedScrollPosition = preferences.getInt("scrollPosition", 0);

        // Thiết lập adapter cho ListView chương
        ChapterAdapter adapter = new ChapterAdapter(this, chapterTitles);
        chapterListView.setAdapter(adapter);

        // Thiết lập bìa sách và tiêu đề
        bookCover.setImageResource(android.R.color.holo_blue_dark);
        bookCoverSaved.setImageResource(android.R.color.holo_blue_dark);
        bookTitle.setText("Kinh tế số");
        bookTitleSaved.setText("Kinh tế số");

        // Hiển thị nội dung chương hiện tại
        updateChapterContent();

        // Khôi phục vị trí cuộn sau khi nội dung đã được hiển thị
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, savedScrollPosition);
            }
        });

        // Thiết lập Spinner cho font chữ
        String[] fonts = {"Times New Roman", "Arial", "Roboto"};
        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fonts);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);

        // Xử lý sự kiện chạm vào scrollView để đóng các panel
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Đóng các panel nếu đang mở
                    if (isBookmarkPanelVisible) {
                        toggleBookmarkPanel();
                    }
                    if (isSavedPositionsPanelVisible) {
                        toggleSavedPositionsPanel();
                    }
                    if (isCustomizePanelVisible) {
                        toggleCustomizePanel();
                    }
                }
                return false; // Trả về false để scrollView vẫn xử lý sự kiện cuộn bình thường
            }
        });

        // Xử lý sự kiện nút back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookmarkPanelVisible) {
                    toggleBookmarkPanel();
                } else if (isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                } else if (isCustomizePanelVisible) {
                    toggleCustomizePanel();
                } else {
                    finish();
                }
            }
        });

        // Xử lý sự kiện nút lưu vị trí (trên cùng)
        savePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scrollPosition = scrollView.getScrollY();
                addBookmark(currentChapter, scrollPosition);
                Toast.makeText(MainActivitybao.this, "Đã lưu vị trí đọc", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nút lưu vị trí (trong bottom navigation)
        savePositionButtonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng customize panel nếu đang mở
                if (isCustomizePanelVisible) {
                    toggleCustomizePanel();
                }

                // Đóng bookmark panel nếu đang mở
                if (isBookmarkPanelVisible) {
                    toggleBookmarkPanel();
                }

                // Mở saved positions panel và hiển thị danh sách bookmarks
                if (!isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                }

                // Hiển thị danh sách bookmarks
                List<Bookmark> bookmarks = getBookmarks();
                BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(MainActivitybao.this, bookmarks);
                bookmarkListView.setAdapter(bookmarkAdapter);

                Toast.makeText(MainActivitybao.this, "Danh sách vị trí đã lưu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nút tùy chỉnh (biểu tượng chữ "A")
        textSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookmarkPanelVisible) {
                    toggleBookmarkPanel();
                }
                if (isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                }
                toggleCustomizePanel();
            }
        });

        // Xử lý sự kiện nút chương trước
        prevChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookmarkPanelVisible) {
                    toggleBookmarkPanel();
                }
                if (isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                }
                if (isCustomizePanelVisible) {
                    toggleCustomizePanel();
                }

                if (currentChapter > 1) {
                    currentChapter--;
                    updateChapterContent();
                    scrollView.scrollTo(0, 0);
                    Toast.makeText(MainActivitybao.this, "Chương " + currentChapter, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivitybao.this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện nút chương tiếp theo
        nextChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookmarkPanelVisible) {
                    toggleBookmarkPanel();
                }
                if (isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                }
                if (isCustomizePanelVisible) {
                    toggleCustomizePanel();
                }

                if (currentChapter < TOTAL_CHAPTERS) {
                    currentChapter++;
                    updateChapterContent();
                    scrollView.scrollTo(0, 0);
                    Toast.makeText(MainActivitybao.this, "Chương " + currentChapter, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivitybao.this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện nút danh sách chương (biểu tượng danh sách)
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCustomizePanelVisible) {
                    toggleCustomizePanel();
                }
                if (isSavedPositionsPanelVisible) {
                    toggleSavedPositionsPanel();
                }
                toggleBookmarkPanel();
            }
        });

        // Xử lý sự kiện nút Home
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivitybao.this, "Nút Home được nhấn", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấp vào một chương
        chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentChapter = position + 1;
                updateChapterContent();
                scrollView.scrollTo(0, 0);
                toggleBookmarkPanel();
                Toast.makeText(MainActivitybao.this, chapterTitles[position], Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấp vào một bookmark
        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmark bookmark = (Bookmark) parent.getItemAtPosition(position);
                currentChapter = bookmark.getChapter();
                final int scrollPosition = bookmark.getScrollPosition();

                updateChapterContent();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollPosition);
                    }
                });

                toggleSavedPositionsPanel();
                Toast.makeText(MainActivitybao.this, "Đã chuyển đến vị trí: Chương " + currentChapter, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện thay đổi màu nền
        colorBlack.setOnClickListener(v -> {
            scrollView.setBackgroundColor(0xFF000000);
            contentText.setTextColor(0xFFFFFFFF);
            chapterTitleText.setTextColor(0xFFFFFFFF);
        });

        colorGreen.setOnClickListener(v -> {
            scrollView.setBackgroundColor(0xFF00FF00);
            contentText.setTextColor(0xFF000000);
            chapterTitleText.setTextColor(0xFF000000);
        });

        colorYellow.setOnClickListener(v -> {
            scrollView.setBackgroundColor(0xFFFFFFCC);
            contentText.setTextColor(0xFF000000);
            chapterTitleText.setTextColor(0xFF000000);
        });

        colorPink.setOnClickListener(v -> {
            scrollView.setBackgroundColor(0xFFFF99CC);
            contentText.setTextColor(0xFF000000);
            chapterTitleText.setTextColor(0xFF000000);
        });

        colorBlue.setOnClickListener(v -> {
            scrollView.setBackgroundColor(0xFF99CCFF);
            contentText.setTextColor(0xFF000000);
            chapterTitleText.setTextColor(0xFF000000);
        });

        // Xử lý sự kiện thay đổi font chữ
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

        // Xử lý sự kiện tăng/giảm cỡ chữ
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

        // Xử lý sự kiện tăng/giảm căn lề
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

    // Adapter tùy chỉnh cho danh sách chương
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

            // Đánh dấu chương hiện tại
            if (position == currentChapter - 1) {
                convertView.setBackgroundColor(0x22000000);
            } else {
                convertView.setBackgroundColor(0x00000000);
            }

            return convertView;
        }
    }

    // Adapter tùy chỉnh cho danh sách bookmarks
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

    // Lấy danh sách bookmarks từ SharedPreferences
    private List<Bookmark> getBookmarks() {
        String bookmarksJson = preferences.getString("bookmarks", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Bookmark>>() {}.getType();
        List<Bookmark> bookmarks = gson.fromJson(bookmarksJson, type);
        if (bookmarks == null) {
            bookmarks = new ArrayList<>();
        }
        return bookmarks;
    }

    // Lưu danh sách bookmarks vào SharedPreferences
    private void saveBookmarks(List<Bookmark> bookmarks) {
        Gson gson = new Gson();
        String bookmarksJson = gson.toJson(bookmarks);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bookmarks", bookmarksJson);
        editor.apply();
    }

    // Thêm một bookmark mới
    private void addBookmark(int chapter, int scrollPosition) {
        List<Bookmark> bookmarks = getBookmarks();
        bookmarks.add(new Bookmark(chapter, scrollPosition));
        saveBookmarks(bookmarks);
    }

    // Cập nhật nội dung chương
    private void updateChapterContent() {
        chapterTitleText.setText(chapterTitles[currentChapter - 1]);
        contentText.setText(chapterContents[currentChapter - 1]);
    }

    // Lưu vị trí đọc hiện tại
    private void saveReadingPosition() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentChapter", currentChapter);
        editor.putInt("scrollPosition", scrollView.getScrollY());
        editor.apply();
    }

    // Hiển thị/ẩn panel bookmark
    private void toggleBookmarkPanel() {
        isBookmarkPanelVisible = !isBookmarkPanelVisible;

        if (isBookmarkPanelVisible) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int panelWidth = screenWidth / 2;

            ViewGroup.LayoutParams panelParams = bookmarkPanel.getLayoutParams();
            panelParams.width = panelWidth;
            bookmarkPanel.setLayoutParams(panelParams);

            bookmarkPanel.setVisibility(View.VISIBLE);
            ((ChapterAdapter) chapterListView.getAdapter()).notifyDataSetChanged();
        } else {
            bookmarkPanel.setVisibility(View.GONE);
        }
    }

    // Hiển thị/ẩn panel saved positions
    private void toggleSavedPositionsPanel() {
        isSavedPositionsPanelVisible = !isSavedPositionsPanelVisible;

        if (isSavedPositionsPanelVisible) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int panelWidth = screenWidth / 2;

            ViewGroup.LayoutParams panelParams = savedPositionsPanel.getLayoutParams();
            panelParams.width = panelWidth;
            savedPositionsPanel.setLayoutParams(panelParams);

            savedPositionsPanel.setVisibility(View.VISIBLE);
        } else {
            savedPositionsPanel.setVisibility(View.GONE);
        }
    }

    // Hiển thị/ẩn panel tùy chỉnh
    private void toggleCustomizePanel() {
        isCustomizePanelVisible = !isCustomizePanelVisible;

        if (isCustomizePanelVisible) {
            customizePanel.setVisibility(View.VISIBLE);
        } else {
            customizePanel.setVisibility(View.GONE);
        }
    }

    // Xử lý khi xoay màn hình
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

    // Lưu vị trí đọc khi thoát ứng dụng
    @Override
    protected void onPause() {
        super.onPause();
        saveReadingPosition();
    }
}