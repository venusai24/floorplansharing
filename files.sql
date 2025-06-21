CREATE TABLE files (
    file_id INT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255),
    uploaded_by VARCHAR(255),
    upload_date DATETIME,
    access ENUM('private', 'public'),
    link VARCHAR(250)
);