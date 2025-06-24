package com.example.BookHeaven.sach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BookHeaven.Danhsachdanhgia.DanhSachDanhGiaActivity;
import com.example.BookHeaven.LibraryManager;
import com.example.BookHeaven.MainActivitybao;
import com.example.BookHeaven.R;
import com.example.BookHeaven.Vietdanhgia.DanhGiaActivity;
import com.example.BookHeaven.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
//AppCompatActivity để xây dựng các activity
public class ChiTietSach extends AppCompatActivity {

    //khai báo lớp va biến
    private Book book;

    private ImageButton favoriteButton; //faavoriteButton để bật/tắt trạng
    private RecyclerView relatedBooksRecyclerView; // hiển thị sách liên quan
    private RelatedBooksAdapter relatedBooksAdapter;  //quản lý hiển thị sách liên quan

    private DatabaseReference databaseReference;//tham chiếu firebase đến node 'books' để lấy dữ liệu

    private TextView reviewLabel; // chuyển huóng đến activity viết đánh giá


    @Override
    //khới tạo activity
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sach);//liên kết đến activity chi tiet sach

        //lấy dữ liệu từ Intent -Intent là một cơ chế để truyền dữ liệu và kích hoạt các than phần như activity
        // khời tạo đối tượng intent để truy xuâts dữ liệu
        // getintent trả về  đối tượng intent
        Intent intent = getIntent(); //Lấy Intent từ màn hình trước (như danh sách sách).
        // biến intent lúc này chứa tâ cả dữ liệu(extras)
        if (intent.hasExtra("book")) //Kiểm tra nếu Intent chứa mộ
        {
            book = (Book) intent.getSerializableExtra("book");
            Log.d("ChiTietSach", "Book received from Intent: " + book.getTitle() + ", Author: " + book.getAuthor());
        } else if (intent.hasExtra("bookId") && intent.hasExtra("bookTitle") && intent.hasExtra("imageUrl")) {
            String bookId = intent.getStringExtra("bookId");
            String bookTitle = intent.getStringExtra("bookTitle");
            String imageUrl = intent.getStringExtra("imageUrl");
            String bookAuthor = intent.getStringExtra("bookAuthor");
            book = new Book(bookId, bookTitle, imageUrl);
            if (bookAuthor != null && !bookAuthor.trim().isEmpty()) {
                book.setAuthor(bookAuthor);
            }
            Log.d("ChiTietSach", "Book created with minimal data, author set to: " + book.getAuthor());
        }
        //nếu intent không chứa Book hoặc thông tin tối thiểu  thì hiển thị thông báp và đóng màn hình
        else {
            Toast.makeText(this, "Không nhận được dữ liệu sách!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //khơi tạo biến orginalAuthor để lưu tác giả ban đâu ftuwf Intent
        String originalAuthor = book.getAuthor();
        Log.d("ChiTietSach", "Original author from Intent: " + originalAuthor);

        //khởi tạo giao diện
        ImageView bookCover = findViewById(R.id.book_cover);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView bookViews = findViewById(R.id.book_views);
        TextView bookLikes = findViewById(R.id.book_likes);
        TextView bookDescription = findViewById(R.id.book_description);
        TextView publicationDate = findViewById(R.id.publication_date);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        TextView averageRating = findViewById(R.id.average_rating);
        TextView readLabel = findViewById(R.id.read_label);
        reviewLabel = findViewById(R.id.review_label);
        Button btnSeeAll = findViewById(R.id.btn_see_all);
        favoriteButton = findViewById(R.id.favoriteButton);
        TextView bookPageCount = findViewById(R.id.book_page_count);
        TextView authorName = findViewById(R.id.author_name);
        TextView authorStats = findViewById(R.id.author_stats);

        //tải ảnh
        Picasso.get()
                .load(book.getImageUrl()) //tải ảnh
                .placeholder(R.drawable.book_khoi_nghiep) //dặt ảnh mặc định khi imageView đang đc tải
                .error(R.drawable.book_tinh_yeu_dau_doi)// đặt ảnh lỗi nếu tải ảnh ỦL thất baih
                .into(bookCover);
        //khởi tạo firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        //nếu nút reviewLabel không rỗng thì hển thị nút viết đánh giá
        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }

        //đuược thực hiện nếu thông tin tác giả là "Unknown Author" hoặc mô tả là null
        if (book.getAuthor().equals("Unknown Author") || book.getDescription() == null) {
            //tham chiếu con đến node cụ thể của một cuốn sách thông qua bookId vì bookid nằm trong nhánh book

            DatabaseReference bookRef = databaseReference.child(book.getId());
            //truy ấn firebase, -> mục đích để lấy dữ liệu đầy đủ của sách
            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                //Gọi khi dữ liệu được truy vấn thành công
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //kiểm tra xem node đó có dữ liệu không
                    if (snapshot.exists()) {
                        // chuyển dl Json tư snapshot thành đối tượng book và lưu vào đối tuượng Book mới
                        // biến firebaseBook kiểu Book
                        //Chuyển ổi dl thành đối tượng book v lueu vào firebasebook
                        Book firebaseBook = snapshot.getValue(Book.class);
                        //ktra xem dl ánh xạ có thành công k?
                        if (firebaseBook != null) {
                            if (book.getAuthor().equals("Unknown Author") && firebaseBook.getAuthor() != null && !firebaseBook.getAuthor().trim().isEmpty()) {
                                book.setAuthor(firebaseBook.getAuthor()); //nếu tác giả k mặc định, không null, không phâir chuỗi rỗng và chứa khoảng trống -> cập nhật tác giả
                                Log.d("ChiTietSach", "Updated author from Firebase: " + book.getAuthor());
                            } else {
                                book.setAuthor(originalAuthor); //khôi phục lại giá trj mặc ịnh ban đầu
                                Log.d("ChiTietSach", "Restored original author: " + originalAuthor);
                            }
                            //cập nhật các thuộc tính còn lại của bok vs dl lấy từ firebaseBook
                            //nếu firebaseBoook có gtrij null thì book sẽ nhận giá trị null
                            book.setDescription(firebaseBook.getDescription());
                            book.setPublicationDate(firebaseBook.getPublicationDate());
                            book.setPageCount(firebaseBook.getPageCount());
                            book.setViews(firebaseBook.getViews());
                            book.setLikes(firebaseBook.getLikes());
                            book.setAverageRating(firebaseBook.getAverageRating());
                            book.setCategory(firebaseBook.getCategory());
                            book.setChapters(firebaseBook.getChapters());

                            //cập nhật giao diện vs dl lấy từ book
                            bookTitle.setText(book.getTitle());
                            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
                            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
                            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
                            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
                            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
                            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));

                            authorName.setText(book.getAuthor() != null ? book.getAuthor() : "Không xác định");
                            authorStats.setText("Thông tin tác giả đang được cập nhật");

                            //xếp hnagj trung bình
                            float roundedRating = 0.0f;
                            try {
                                String avgRatingStr = String.valueOf(book.getAverageRating()).replace(",", ".");
                                roundedRating = Float.parseFloat(avgRatingStr);
                            } catch (NumberFormatException e) // nếu xếp hạng trung bình k ở định dạng hợp lê thif sex hiển thị log
                            {
                                Log.e("ChiTietSach", "Error parsing averageRating: " + e.getMessage());
                            }
                            ratingBar.setRating(roundedRating > 0 ? roundedRating : 0.0f);
                            averageRating.setText(String.format("%.1f", roundedRating));

                            //sử dụng listener để  cập nhataj nút  yêu thích
                            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                                updateFavoriteButton();
                            }));

                            //hiển thị nhãn đánh giá
                            if (reviewLabel != null) {
                                reviewLabel.setVisibility(View.VISIBLE);
                            }

                            loadRelatedBooks();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ChiTietSach", "Lỗi tải dữ liệu sách: " + error.getMessage());
                    Toast.makeText(ChiTietSach.this, "Lỗi tải dữ liệu sách: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else //author k null
        {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText("Tác giả: " + (book.getAuthor() != null ? book.getAuthor() : "Không xác định"));
            bookViews.setText(book.getViews() != null ? book.getViews() : "0");
            bookLikes.setText(book.getLikes() != null ? book.getLikes() : "0");
            bookPageCount.setText(String.valueOf(book.getPageCount() != 0 ? book.getPageCount() : "Không xác định"));
            bookDescription.setText(book.getDescription() != null ? book.getDescription() : "Không có mô tả");
            publicationDate.setText("Ngày xuất bản: " + (book.getPublicationDate() != null ? book.getPublicationDate() : "Không xác định"));

            authorName.setText(book.getAuthor() != null ? book.getAuthor() : "Không xác định");
            authorStats.setText("Thông tin tác giả đang được cập nhật");

            float roundedRating = 0.0f;
            try {
                String avgRatingStr = String.valueOf(book.getAverageRating()).replace(",", ".");
                roundedRating = Float.parseFloat(avgRatingStr);
            } catch (NumberFormatException e) {
                Log.e("ChiTietSach", "Error parsing averageRating: " + e.getMessage());
            }
            ratingBar.setRating(roundedRating > 0 ? roundedRating : 0.0f);
            averageRating.setText(String.format("%.1f", roundedRating));

            LibraryManager.getInstance().setOnDataLoadedListener(() -> runOnUiThread(() -> {
                updateFavoriteButton();
            }));

            loadRelatedBooks();
        }

        //khởi tạo recyclerView cho các sách liên quan
        relatedBooksRecyclerView = findViewById(R.id.related_books_recycler_view);
        relatedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedBooksAdapter = new RelatedBooksAdapter(this, new ArrayList<>());
        relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);

        //sử lý sự kiện đọc sách
        readLabel.setOnClickListener(v -> {
            if (book == null || book.getChapters() == null || book.getChapters().isEmpty()) {
                Toast.makeText(ChiTietSach.this, "Không có nội dung để đọc", Toast.LENGTH_SHORT).show();
                return;
            }

            LibraryManager libraryManager = LibraryManager.getInstance();
            //tăng lượt đọc
            libraryManager.incrementBookViews(book);
            bookViews.setText(book.getViews());

            //thêm vào lịch sử
            if (libraryManager.addToReadingList(ChiTietSach.this, book)) {
                Toast.makeText(ChiTietSach.this, "Đã thêm vào lịch sử đọc", Toast.LENGTH_SHORT).show();
            }

            Intent readIntent = new Intent(ChiTietSach.this, MainActivitybao.class);
            readIntent.putExtra("book", book);
            startActivity(readIntent);
        });

        if (reviewLabel != null) {
            reviewLabel.setOnClickListener(v -> {
                if (book == null || book.getId() == null) {
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent reviewIntent = new Intent(ChiTietSach.this, DanhGiaActivity.class);
                reviewIntent.putExtra("book_id", book.getId());
                reviewIntent.putExtra("book_title", book.getTitle());
                reviewIntent.putExtra("book_author", book.getAuthor());
                reviewIntent.putExtra("book_cover", book.getImageUrl());
                startActivity(reviewIntent);
            });
        }

        //xử l sự kiện khi xem tất cả đánh giá
        btnSeeAll.setOnClickListener(v -> {
            if (book == null) {
                Toast.makeText(ChiTietSach.this, "Không tìm thấy dữ liệu sách!", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Intent seeAllIntent = new Intent(ChiTietSach.this, DanhSachDanhGiaActivity.class);
                    seeAllIntent.putExtra("book_id", book.getId());
                    seeAllIntent.putExtra("book_title", book.getTitle());
                    seeAllIntent.putExtra("book_author", book.getAuthor());
                    seeAllIntent.putExtra("book_cover", book.getImageUrl());
                    seeAllIntent.putExtra("average_rating", book.getAverageRating());
                    startActivity(seeAllIntent);
                } catch (Exception e) {
                    Toast.makeText(ChiTietSach.this, "Lỗi khi mở DanhSachDanhGiaActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        favoriteButton.setOnClickListener(v -> {
            LibraryManager libraryManager = LibraryManager.getInstance();
            boolean isFavorite = libraryManager.isBookFavorite(book.getId());
            if (isFavorite) {
                if (libraryManager.removeFromFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    updateFavoriteButton();
                    bookLikes.setText(book.getLikes());
                }
            } else {
                if (libraryManager.addToFavorites(ChiTietSach.this, book)) {
                    Toast.makeText(ChiTietSach.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    updateFavoriteButton();
                    bookLikes.setText(book.getLikes());
                }
            }
        });

        updateBookStats();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (book != null && book.getId() != null) {
            updateFavoriteButton();
        }

        if (reviewLabel != null) {
            reviewLabel.setVisibility(View.VISIBLE);
        }
    }

    private void updateFavoriteButton() {


        boolean isFavorite = LibraryManager.getInstance().isBookFavorite(book.getId());
        Log.d("ChiTietSach", "Updating favorite button for book " + book.getId() + ". Is favorite: " + isFavorite);
        favoriteButton.setImageResource(isFavorite ? R.drawable.tim_day : R.drawable.tim);
    }

    private void loadRelatedBooks() {
        if (book == null || book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            Log.e("ChiTietSach", "Book or author is null/empty, book: " + (book != null ? book.getTitle() : "null") + ", author: " + (book != null ? book.getAuthor() : "null"));
            relatedBooksAdapter.updateBooks(new ArrayList<>());
            relatedBooksRecyclerView.setVisibility(View.GONE);
            return;
        }

        String currentAuthor = book.getAuthor().trim().toLowerCase();
        Log.d("ChiTietSach", "Loading related books for book: " + book.getTitle() + ", Author (normalized): " + currentAuthor);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Book> relatedBooks = new ArrayList<>();
                Log.d("ChiTietSach", "Total books in Firebase: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book b = bookSnapshot.getValue(Book.class);
                    if (b != null && b.getId() != null && b.getAuthor() != null && !b.getAuthor().trim().isEmpty()) {
                        String firebaseAuthor = b.getAuthor().trim().toLowerCase();
                        if (!b.getId().equals(book.getId()) && firebaseAuthor.equals(currentAuthor)) {
                            relatedBooks.add(b);
                            Log.d("ChiTietSach", "Added related book: " + b.getTitle() + " by " + b.getAuthor());
                        }
                    }
                }

                Log.d("ChiTietSach", "Total related books found: " + relatedBooks.size() + " for author: " + currentAuthor);
                relatedBooksAdapter.updateBooks(relatedBooks);
                relatedBooksRecyclerView.setVisibility(relatedBooks.isEmpty() ? View.GONE : View.VISIBLE);

                if (relatedBooks.isEmpty()) {
                    Log.w("ChiTietSach", "No related books found for author: " + currentAuthor);
                    Toast.makeText(ChiTietSach.this, "Không tìm thấy sách cùng tác giả.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChiTietSach", "Error loading related books: " + databaseError.getMessage());
                Toast.makeText(ChiTietSach.this, "Lỗi tải sách liên quan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                relatedBooksAdapter.updateBooks(new ArrayList<>());
                relatedBooksRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void updateBookStats() {
        if (book != null && book.getId() != null) {
            DatabaseReference bookRef = databaseReference.child(book.getId());
            bookRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) //kiểm tra xem sách có tồn tịa trong dữ lệu k
                    {
                        if (snapshot.child("views").exists()) //kiểm tra xem trường view có trong node sách k
                        {
                            String views = snapshot.child("views").getValue(String.class); //lấy giá trị của views du dạng chuỗi
                            if (views != null) {
                                book.setViews(views);
                                TextView bookViews = findViewById(R.id.book_views);
                                if (bookViews != null) {
                                    bookViews.setText(views);
                                }
                            }
                        }

                        if (snapshot.child("likes").exists()) {
                            String likes = snapshot.child("likes").getValue(String.class);
                            if (likes != null) {
                                book.setLikes(likes);
                                TextView bookLikes = findViewById(R.id.book_likes);
                                if (bookLikes != null) {
                                    bookLikes.setText(likes);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ChiTietSach", "Error updating book stats: " + error.getMessage());
                }
            });
        }
    }
}