defmodule ServerWeb.SwapController do
  use ServerWeb, :controller

  def random(conn, _params) do
    swaps = SwapGenerator.run()
    render(conn, "show.json", swaps: swaps)
  end
end
