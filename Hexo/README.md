# Hexo 博客

*松下问童子，言师采药去。只在此山中，云深不知处*。

```yml
- name: Clone File
    run: |
    cd ~
    git clone git@github.com:inkss/markdown.git markdown
    rm -rf markdown/Hexo
    mkdir -p markdown/Hexo
    cp -R work/Hexo-Blog/Hexo-Blog/source/* markdown/Hexo
- name: Sync File Markdown
    run: |
    cd ~/markdown
    git status > ~/markdown.txt
    if grep "nothing to commit" ~/markdown.txt
    then
        echo "Nothing to commit, working tree clean~"
    else
        cd ~/markdown
        echo "Auto Sync File"
        git add .
        git commit -m 'Auto Sync File'
        git push -f
    fi
```
