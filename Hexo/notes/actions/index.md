---
layout: page
seo_title: Github Actions
sidebar: [wiki-hexo-theme, toc]
meta:
  header: []
  footer: [updated, counter]
date: 2020/06/05 15:12
updated: 2020/06/12 09:34
---

{% p center logo large, Github Actions %}

{% note alien-monster gray, Github Actions，可以简单的理解为是一个台免费服务器：性能强大的服务器，有每月最大时间限制。 %}

### Hexo 博客自动部署

```yml
name: Hexo Action
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    name: A job to deploy blog.
    steps:
    - name: Checkout
      uses: actions/checkout@v2
      with:
        submodules: true
    - name: Cache node modules
      uses: actions/cache@v1
      id: cache
      with:
        path: node_modules
        key: ${{ runner.os }}-node-${{ xhashFiles('**/package-lock.json') }}
        restore-keys: |
          ${{ runner.os }}-node-
    - name: Install Dependencies
      if: steps.cache.outputs.cache-hit != 'true'
      run: npm ci
    - name: Deploy
      id: deploy
      uses: sma11black/hexo-action@v1.0.1
      with:
        deploy_key: ${{ secrets.DEPLOY_KEY }}
    - name: Setup Deploy Private Key
      env:
        DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
      run: |
        mkdir -p ~/.ssh/
        echo "$DEPLOY_KEY" > ~/.ssh/id_rsa
        chmod 600 ~/.ssh/id_rsa
        ssh-keyscan github.com >> ~/.ssh/known_hosts
        git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"
        git config --global user.name "github-actions[bot]"
    - name: Clone File
      run: |
        cd ~
        git clone git@github.com:inkss/markdown.git markdown
        git clone git@github.com:inkss/common.git common
        rm -rf markdown/Hexo
        rm -rf common/hexo/js
        rm -rf common/hexo/css
        mkdir -p markdown/Hexo
        mkdir -p common/hexo/js
        mkdir -p common/hexo/css
        cp -R work/Hexo-Blog/Hexo-Blog/source/* markdown/Hexo
        cp -R work/Hexo-Blog/Hexo-Blog/public/js/* common/hexo/js
        cp -R work/Hexo-Blog/Hexo-Blog/public/css/* common/hexo/css
    - name: Sync Markdown
      run: |
        cd ~/markdown
        git status > ~/markdown.txt
        if grep "nothing to commit" ~/markdown.txt
        then
          echo ">>> MARKDOWN: The package is clean~"
        else
          cd ~/markdown
          echo "Auto Sync File"
          git add .
          git commit -m 'Auto Sync File'
          git push -f
        fi
    - name: Sync Common
      run: |
        cd ~/common
        git status > ~/common.txt
        if grep "nothing to commit" ~/common.txt
        then
          echo ">>> COMMON: The package is clean~"
        else
          cd ~/common
          echo "Auto Sync File"
          git add .
          git commit -m 'Auto Sync File'
          git push -f
        fi
    - name: Get the output
      run: |
        echo "${{ steps.deploy.outputs.notify }}"
```

### Leancloud 定时唤醒

```yml
name: AutoWakeUp
on:
  schedule:
    - cron: "*/20 23,0-15 * * *"
  push:
    branches:
      - master
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Setup Deploy Private Key
        env:
          DEPLOY_KEY: ${{ secrets.TOKEN }}
        run: |
          mkdir -p ~/.ssh/
          echo "$DEPLOY_KEY" > ~/.ssh/id_rsa
          chmod 600 ~/.ssh/id_rsa
          ssh-keyscan github.com >> ~/.ssh/known_hosts
          git config --global user.email "41898282+github-actions[bot]@users.noreply.github.com"
          git config --global user.name "github-actions[bot]"
      - name: Clone && Curl
        run: |
          git clone git@github.com:inkss/inkss.github.io.git ~/inkss
          cd ~/inkss
          git checkout action
          git reset --soft efb41ea0f3b645898448713a4348170bfaeb5b00
          curl -sS --connect-timeout 10 -m 7200 'https://info.inkss.cn/' > readme.txt
          echo "----------------------------------------------------------------------------" >> readme.txt
          endDate=`date -d '+8 hours' +'%Y-%m-%d %H:%M:%S'`
          echo "★ [$endDate] Successful" >> readme.txt
          echo "----------------------------------------------------------------------------" >> readme.txt
      - name: Commit
        run: |
          cd ~/inkss
          git add .
          git commit -m "Successfully Wake - $(git rev-parse --short HEAD)" -a
          git push -f
```
