use pap22Z_z03;

CREATE TABLE Settings (
    ProfileID INT NOT NULL,
    Name VARCHAR(40) NOT NULL,
    MusicVolume INT NOT NULL,
    SFXVolume INT NOT NULL,
    GameSpeed DECIMAL(3, 2) NOT NULL,
    GameLength INTEGER
);