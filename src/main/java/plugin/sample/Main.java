package plugin.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

public final class Main extends JavaPlugin implements Listener {

  private int allUsrSneakCnt = 0;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    if (allUsrSneakCnt % 2 == 0) {
      // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
      for (int i = 0; i < 10; i++) {
        spawnFireWork(world, player);
      }
//      Component comp = Component.text(allUsrSneakCnt);
//      Bukkit.getServer().sendMessage(comp);
      Path path = Path.of("firework.txt");
      Files.writeString(path, "たーまやー", StandardOpenOption.APPEND);
      player.sendMessage(Files.readString(path));
    }

    allUsrSneakCnt++;
  }

  private void spawnFireWork(World world, Player player) {
    Firework firework = world.spawn(player.getLocation(), Firework.class);

    // 花火オブジェクトが持つメタ情報を取得。
    FireworkMeta fireworkMeta = firework.getFireworkMeta();

    // メタ情報に対して設定を追加したり、値の上書きを行う。
    // 今回は青色で星型の花火を打ち上げる.
    fireworkMeta.addEffect(
        FireworkEffect.builder()
            .withColor(Color.RED)
            .withColor(Color.BLUE)
            .withColor(Color.AQUA)
            .with(Type.BALL_LARGE)
            .withFlicker()
            .build());
    fireworkMeta.setPower(3);
    // 追加した情報で再設定する。
    firework.setFireworkMeta(fireworkMeta);
  }

  @EventHandler
  public void onPlayerBedEnterEvent(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        .filter(item -> !Objects.isNull(item) && item.getAmount() != item.getMaxStackSize())
        .forEach(item -> item.setAmount(item.getMaxStackSize()));
    player.getInventory().setContents(itemStacks);

  }


}
