use pap22Z_z03;

CREATE TABLE Settings (
    ProfileID INT UNIQUE NOT NULL,
    Name VARCHAR(40) UNIQUE NOT NULL,
    MusicVolume INT NOT NULL,
    SFXVolume INT NOT NULL,
    GameSpeed DECIMAL(3, 2) NOT NULL,
    GameLength INTEGER NOT NULL
);