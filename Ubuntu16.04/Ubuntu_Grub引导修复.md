# 修复grub引导

## **适用情况**：

    error: unknown filesystem
    Ending rescue mode...
    grub rescue>

## **错误原因**：

  grub配置文件错误，由于分区调整或分区UUID改变造成grub2不能启动，从而进入修复模式，亦称救援模式。

  此模式下，可供使用的命令仅有：`set, ls, insmod, root, prefix.`

## **解决方案**：

 1.寻找Ubuntu所在分区，终端执行`ls`命令，查看分区表。

其结果类似于：`（hd0) (hd1) (hd1,xxx1) (hd1,xxx2) ...`

    grub rescue>ls
    (hd0) (hd1) (hd1,gpt1) (hd1,gpt2) (hd1,gpt3) (hd1,gpt4)

2.继续通过`ls`命令查看具体的Ubuntu所在分区(这个要根据**反馈结果**来判断)。

    grub rescue>ls (hd1,gpt1)/boot/grub

3.假设`(hd1,hpt6)`为系统所在分区，则现在去配置grub引导。

    grub rescue>set root=(hd1,gpt6)
    grub rescue>set prefix=(hd1,gpt6)/boot/grub

4.接下来切换到normal模式。

    grub rescue>insmod normal
    grub rescue>normal

5.在正常情况下,此时就可以正常进入系统了。

---

### 附录1. 测试机型为legacy引导的Ubuntu系统

### 附录2. 代码中 “`grub rescue>`” 为提示符，无需输入。