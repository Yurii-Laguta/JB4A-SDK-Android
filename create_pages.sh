#!/bin/bash
# The install script for my vim dotfiles.
# Find the location of the script.

# The source directory and target directories.
SOURCE_LOCATION="_posts" # Contains the posts with dates
TARGET_LOCATION="_pages" # Contains the symlinks of _posts without the date

if [ -d "$TARGET_LOCATION" ]; then
  # remove this folder
  rm -rf "$TARGET_LOCATION"
fi

mkdir "$TARGET_LOCATION"

# Link the files from source to the target with a dot appended to the front.
#find $SOURCE_LOCATION -mindepth 1 -maxdepth 1 -printf "%P\n" | while read file; do
#    echo "Linking $SOURCE_LOCATION/$file to $TARGET_LOCATION/$file"
#    ln -s "$SOURCE_LOCATION/$file" "$TARGET_LOCATION/$file"
#done

for file in $(ls $SOURCE_LOCATION)
  do 
      echo "Linking $SOURCE_LOCATION/$file to symlink: $TARGET_LOCATION/${file:11}"; 
      ln -s "../$SOURCE_LOCATION/$file" "$TARGET_LOCATION/${file:11}"
  done