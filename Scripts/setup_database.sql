USE `pap22Z_z03`;

DROP VIEW IF EXISTS ResultsStatistics;
DROP TABLE IF EXISTS CurrentProfile;
DROP TABLE IF EXISTS Results;
DROP TABLE IF EXISTS StatResults;
DROP TABLE IF EXISTS Settings;

/* This table holds the data about profiles and the settings associated
 * with them. */
CREATE TABLE Settings (
    ProfileID INT UNIQUE NOT NULL AUTO_INCREMENT,
    Name VARCHAR(60) UNIQUE NOT NULL,
    MusicVolume INT NOT NULL,
    SFXVolume INT NOT NULL,
    GameDifficulty INT NOT NULL,
    GameLength INT NOT NULL,
    PRIMARY KEY(ProfileID)
) ENGINE=InnoDB;
INSERT INTO Settings VALUES(NULL, 'default', 50, 50, 2, 20);

/* This table holds the statistical data of the results, for calculation
 * of averages. */
CREATE TABLE StatResults (
    StatResultID INT UNIQUE NOT NULL AUTO_INCREMENT,
    Score INT NOT NULL,
    Accuracy DECIMAL(5, 2) NOT NULL,
    GameType VARCHAR(20) NOT NULL,
    PRIMARY KEY(StatResultID)
) ENGINE=InnoDB;

/* This table holds the results as they are displayed on the scoreboard. */
CREATE TABLE Results (
    ResultID INT UNIQUE NOT NULL AUTO_INCREMENT,
    GameTime TIMESTAMP NOT NULL,
    GameDifficulty INT NOT NULL,
    GameLength INT NOT NULL,
    ProfileID INT NOT NULL,
    StatResultID INT NOT NULL,
    PRIMARY KEY(ResultID),
    CONSTRAINT FOREIGN KEY(ProfileID)
        REFERENCES Settings(ProfileID)
        ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY(StatResultID)
        REFERENCES StatResults(StatResultID)
        ON DELETE CASCADE
) ENGINE=InnoDB;

/* This table holds the data about the current profile. There should be
 * only one row. */
CREATE TABLE CurrentProfile (
    CurrentProfileID INT UNIQUE NOT NULL AUTO_INCREMENT,
    ProfileID INT UNIQUE,
    PRIMARY KEY(CurrentProfileID),
    CONSTRAINT FOREIGN KEY(ProfileID)
        REFERENCES Settings(ProfileID)
        ON DELETE SET NULL
) ENGINE=InnoDB;
INSERT INTO CurrentProfile VALUES(NULL, 1);

/* This view calculates the average score and accuracy for each game mode. */
CREATE VIEW ResultsStatistics
AS
SELECT GameType,
       AVG(Score) AS AverageScore,
       AVG(Accuracy) AS AverageAccuracy,
       COUNT(*) AS NumberOfGames
FROM StatResults
GROUP BY GameType;
