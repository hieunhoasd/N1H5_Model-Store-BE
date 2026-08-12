Kiến trúc hệ thống:

  Postgre: Lưu dữ liệu chính (Users, Products, Orders, Reviews, Wishlist, Inventory, Loyalty...).  
  Redis: Lưu dữ liệu cache và Jwt để truy cập nhanh.  
  Cloudinary: Lưu toàn bộ hình ảnh (sản phẩm, avatar, review), Postgre chỉ lưu Image_URL và Public_Id.  
  React: Giao diện người dùng.  
  Spring Boot: REST API và xử lý nghiệp vụ
  Swagger : tài liệu API.
  Flyway : migration DB...
  
Hệ thống gồm các role chính: Admin, Product Manager,   Warehouse Staff, Order Staff, Content Staff, Customer (User), Guest 
