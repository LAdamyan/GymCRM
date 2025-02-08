CREATE TABLE IF NOT EXISTS users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       isActive BOOLEAN ,
                       DTYPE VARCHAR(20),
                       birth_date DATE,
                       city VARCHAR(50),
                       street VARCHAR(100),
                       building VARCHAR(50),
                       buildingNumber VARCHAR(10)
);


CREATE TABLE IF NOT EXISTS trainings (
                                         id INT PRIMARY KEY AUTO_INCREMENT,
                                         duration INT NOT NULL,
                                         training_date DATETIME NOT NULL,
                                         training_name VARCHAR(100) NOT NULL,
                                         training_type_id INT NOT NULL,
                                         trainee_id INT,
                                         trainer_id INT

);





