#!/bin/bash

set -e

grep -InR 'TODO' ./* \
  --exclude-dir=node_modules \
  --exclude-dir=public \
  --exclude-dir=vendor \
  --exclude-dir=compiled \
  --exclude=list_todo.sh \
  --exclude-dir=git-hooks

exit 0
