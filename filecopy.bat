@echo off
echo "%~1"
pscp -i Morocat.ppk "%~1" ec2-user@ec2-54-215-250-33.us-west-1.compute.amazonaws.com:/home/ec2-user/Lyric