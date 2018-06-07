#!/usr/bin/env bash
# full deploy of firebase to dev and qa; prod must be deployed to manually
firebase use dev
firebase deploy
firebase use qa
firebase deploy
firebase use dev # go back to dev