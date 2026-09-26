CREATE TABLE auth.User_Social_Accounts (
    Social_Id SERIAL PRIMARY KEY,
    User_Id INT NOT NULL,
    Provider_Name VARCHAR(20) NOT NULL,
    Provider_User_Id VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NULL,
    Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Ràng buộc khóa ngoại
    CONSTRAINT FK_Social_Users FOREIGN KEY (User_Id)
        REFERENCES auth.Users(User_Id) ON DELETE CASCADE,

    -- Ràng buộc khóa duy nhất
    CONSTRAINT UQ_Provider_User UNIQUE (Provider_Name, Provider_User_Id)
);