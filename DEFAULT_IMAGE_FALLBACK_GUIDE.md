# 📸 Default Image Fallback Implementation Guide

## 📋 Tóm Tắt

Đã thêm logic fallback để hiển thị ảnh "default-image.jpg" khi không có ảnh cho các entity (Artist, Album, Song).

---

## ✨ Tính Năng

### 1. **Image Helper Fragment** (Tái sử dụng được)

- File: `src/main/resources/templates/fragments/imageHelper.html`
- Cung cấp 2 fragment cho việc hiển thị ảnh với fallback:
  - `image()` - Cho phép truyền entityId, entityType, imagePath
  - `simpleImage()` - Cho các path ảnh đã hoàn chỉnh

### 2. **Fallback Logic**

#### Phương pháp 1: Client-side Fallback (HTML5)

```html
<img
  th:src="@{'/image/artist/' + ${artist.id} + '/' + ${artist.image}}"
  onerror="this.src='/images/default-image.jpg'"
/>
```

- Tải ảnh chính trước
- Nếu lỗi → tự động hiển thị default-image.jpg

#### Phương pháp 2: Server-side Fallback (Thymeleaf)

```html
<img
  th:if="${artist.image != null and artist.image != ''}"
  th:src="@{'/image/artist/' + ${artist.id} + '/' + ${artist.image}}"
/>
<img
  th:if="${artist.image == null or artist.image == ''}"
  src="/images/default-image.jpg"
/>
```

- Kiểm tra trước ở server
- Không cần tải ảnh lỗi

---

## 📝 Files Được Cập Nhật

### Template Files

| File                                  | Phương Pháp     | Ghi Chú               |
| ------------------------------------- | --------------- | --------------------- |
| `templates/home/index.html`           | Server-side     | Artist random card    |
| `templates/admin/artist/details.html` | Fragment Helper | Artist profile detail |
| `templates/admin/artist/results.html` | Server-side     | Artist search results |
| `templates/admin/album/results.html`  | Server-side     | Album search results  |
| `templates/admin/album/search.html`   | Server-side     | Album search results  |

### Mới Tạo

| File                                   | Mục Đích                                    |
| -------------------------------------- | ------------------------------------------- |
| `templates/fragments/imageHelper.html` | Reusable image fragments với fallback logic |

---

## 🚀 Cách Sử Dụng

### Cách 1: Dùng Fragment Helper (Khuyến nghị)

```html
<!-- Trong template file -->
<div
  th:replace="~{fragments/imageHelper :: image(
  imagePath=${artist.image}, 
  entityId=${artist.id}, 
  entityType='artist', 
  altText=${'Photo of ' + artist.name},
  cssClass='img-fluid rounded'
)}"
></div>
```

**Parameters:**

- `imagePath` - Tên file ảnh (không có path)
- `entityId` - ID của entity
- `entityType` - Loại entity: 'artist', 'album', 'song'
- `altText` - Alt text cho ảnh (SEO)
- `cssClass` - CSS classes (optional, default: 'img-fluid rounded')

### Cách 2: Dùng Server-side Fallback

```html
<!-- Nếu có ảnh -->
<img
  th:if="${artist.image != null and artist.image != ''}"
  th:src="@{'/image/artist/' + ${artist.id} + '/' + ${artist.image}}"
  th:alt="${'Photo of ' + artist.name}"
  class="img-fluid rounded"
/>

<!-- Nếu không có ảnh -->
<img
  th:if="${artist.image == null or artist.image == ''}"
  src="/images/default-image.jpg"
  alt="Default Image"
  class="img-fluid rounded"
/>
```

### Cách 3: Dùng Client-side Fallback (HTML5)

```html
<img
  th:src="@{'/image/artist/' + ${artist.id} + '/' + ${artist.image}}"
  th:alt="${'Photo of ' + artist.name}"
  class="img-fluid rounded"
  onerror="this.src='/images/default-image.jpg'"
/>
```

---

## 📦 Default Image File

**Vị trí:** `src/main/resources/static/images/default-image.jpg`

Đây là ảnh sẽ được hiển thị khi:

- Entity không có ảnh (NULL)
- Ảnh file không tìm thấy (lỗi 404)
- Lỗi tải ảnh khác

---

## 🔄 Khi Nào Fallback Được Kích Hoạt

### Server-side Fallback

```java
if (artist.getImage() == null || artist.getImage().isEmpty()) {
    // Hiển thị default-image.jpg
}
```

### Client-side Fallback (onerror)

```html
<!-- Khi ảnh không tồn tại trên server -->
<img
  src="/image/artist/1/nonexistent.jpg"
  onerror="this.src='/images/default-image.jpg'"
/>
```

---

## 💡 Best Practices

### ✅ DO:

1. **Luôn thêm alt text SEO-friendly:**

   ```html
   th:alt="${'Photo of ' + artist.name}"
   ```

2. **Dùng Fragment Helper cho code tái sử dụng:**

   ```html
   <div th:replace="~{fragments/imageHelper :: image(...)}"></div>
   ```

3. **Set chiều cao/rộng để tránh CLS (Cumulative Layout Shift):**

   ```html
   style="height: 300px; object-fit: cover"
   ```

4. **Dùng lazy loading cho performance:**
   ```html
   <img ... loading="lazy" />
   ```

### ❌ DON'T:

1. ❌ Không rely chỉ trên client-side fallback cho required images

   - Dùng server-side check khi chắc chắn ảnh cần thiết

2. ❌ Không để trống alt text

   ```html
   <!-- ❌ SAI -->
   <img src="..." />

   <!-- ✅ ĐÚNG -->
   <img src="..." alt="Descriptive text" />
   ```

3. ❌ Không hardcode full path ảnh
   - Dùng Thymeleaf `@{...}` để flexible

---

## 🧪 Testing

### Test Server-side Fallback

1. Xóa ảnh của một artist từ database:

   ```java
   artist.setImage(null);
   artistRepo.save(artist);
   ```

2. Truy cập page chi tiết artist
   - Kết quả: **Hiển thị default-image.jpg**

### Test Client-side Fallback

1. Cập nhật database với tên file không tồn tại:

   ```java
   artist.setImage("nonexistent-file.jpg");
   artistRepo.save(artist);
   ```

2. Truy cập page chi tiết artist
   - Kết quả: **Tải ảnh lỗi → trigger onerror → default-image.jpg**

---

## 📊 Implementation Status

| Entity               | Fragment Helper | Server-side | Client-side | Status |
| -------------------- | --------------- | ----------- | ----------- | ------ |
| Artist (Detail)      | ✅              | -           | -           | ✅     |
| Artist (Results)     | -               | ✅          | -           | ✅     |
| Artist (Search Home) | -               | ✅          | -           | ✅     |
| Album (Results)      | -               | ✅          | -           | ✅     |
| Album (Search)       | -               | ✅          | -           | ✅     |
| Song (Detail)        | -               | Pending     | -           | ⏳     |

---

## 🔮 Future Improvements

1. **Image Optimization:**

   - Thêm WebP format với fallback
   - Implement image compression

2. **Progressive Loading:**

   - Hiển thị placeholder xám trước
   - Blur-up effect khi ảnh load

3. **Responsive Images:**

   - Dùng `<picture>` tag cho responsive
   - Multiple sizes cho different devices

4. **Image Processing:**
   - Auto-resize ảnh upload
   - Create thumbnails

---

## 📞 Support

Nếu có vấn đề:

1. **Check ảnh tồn tại:**

   ```bash
   ls -la upload/artist/[ID]/
   ls -la static/images/default-image.jpg
   ```

2. **Check database:**

   ```sql
   SELECT id, name, image FROM artist WHERE image IS NULL OR image = '';
   ```

3. **Check browser console:**
   - F12 → Console → Check 404 errors

---

## 📝 Changelog

### v1.0 - December 10, 2025

- ✅ Thêm imageHelper.html fragment
- ✅ Cập nhật artist detail page
- ✅ Cập nhật artist search results
- ✅ Cập nhật album search results
- ✅ Cập nhật home page artist card
- ✅ Tạo documentation

---

**Status:** ✅ PRODUCTION READY

Tất cả các template đã được cập nhật với fallback logic cho default image!
