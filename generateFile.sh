#!/bin/bash

# grant execution access to script (from terminal) if needed
# chmod +x generateFile.sh


# Array of words to be used in the file
words=("beagle" "bear" "someone" "delta" "epsilon" "meh" "zygote" "theta" "iota" "kappa" "Labrador" "sly" "presumptuous" "lynx" "fallout" "Massachusetts" "Mississippi" "quintessential" "falcon" "adventure" "synergy" "paradox" "nebula" "rendezvous" "serendipity" "galaxy" "solstice" "ephemeral" "wanderlust" "chimera")

# Number of lines to be generated
lines=10000

# Output file path
outFile="/workspaces/file-analyzer/largeFile.txt"

# Ensure the output directory exists
mkdir -p "$(dirname "$outFile")"

# Generate the file
for ((i=1; i<=lines; i++)); do
    line=""
    numWords=$((RANDOM % 10 + 5))
    for ((j=1; j<=numWords; j++)); do
        word=${words[RANDOM % ${#words[@]}]}
        line+="$word "
    done
    echo "$line" >> "$outFile"
done
