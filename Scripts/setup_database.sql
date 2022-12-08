USE `pap22Z_z03`;

DROP TABLE IF EXISTS CurrentProfile;
DROP TABLE IF EXISTS Results;
DROP TABLE IF EXISTS Settings;

CREATE TABLE Settings (
    ProfileID INT UNIQUE NOT NULL AUTO_INCREMENT,
    Name VARCHAR(60) UNIQUE NOT NULL,
    MusicVolume INT NOT NULL,
    SFXVolume INT NOT NULL,
    GameDifficulty INT NOT NULL,
    GameLength INT NOT NULL,
    PRIMARY KEY(ProfileID)
);
INSERT INTO Settings VALUES(NULL, 'default', 50, 50, 2, 20);

CREATE TABLE Results (
    ResultID INT UNIQUE NOT NULL AUTO_INCREMENT,
    Score INT NOT NULL,
    Accuracy NUMERIC NOT NULL,
    GameTime TIMESTAMP NOT NULL,
    GameType VARCHAR(20) NOT NULL,
    GameDifficulty INT NOT NULL,
    GameLength INT NOT NULL,
    ProfileID INT NOT NULL REFERENCES Settings(ProfileID)
);

CREATE TABLE CurrentProfile (
    CurrentProfileID INT UNIQUE NOT NULL AUTO_INCREMENT,
    ProfileID INT UNIQUE NOT NULL REFERENCES Settings(ProfileID)
);
INSERT INTO CurrentProfile VALUES(NULL, 1);
