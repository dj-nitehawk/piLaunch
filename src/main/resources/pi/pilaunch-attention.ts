import type { ExtensionAPI } from "@earendil-works/pi-coding-agent";

export default function (pi: ExtensionAPI) {
  pi.on("agent_end", async (_event, ctx) => {
    const url = process.env.PI_LAUNCH_NOTIFY_URL;
    const token = process.env.PI_LAUNCH_NOTIFY_TOKEN;

    if (!url || !token) return;

    await fetch(url, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        "x-pilaunch-token": token,
      },
      body: JSON.stringify({
        type: "agent_end",
        cwd: ctx.cwd,
        time: Date.now(),
      }),
    }).catch(() => {
      // Notification bridge is best-effort. Never break Pi if Rider is gone.
    });
  });
}
