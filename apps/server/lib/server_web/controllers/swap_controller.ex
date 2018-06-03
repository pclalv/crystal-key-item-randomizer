defmodule ServerWeb.SwapController do
  use ServerWeb, :controller

  def random(conn, _params) do
    swap = LockDetector.run()
    render(conn, "show.json", swap: swap)
  end
end
