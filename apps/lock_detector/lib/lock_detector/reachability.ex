defmodule LockDetector.Reachability do
  @locks [:ss_locked?, :kanto_locked?, :goldenrod_locked?, :surf_locked?, :tree_locked?]
  @enforce_keys @locks
  defstruct @locks

  def analyze(swaps) do
    %LockDetector.Reachability{
      ss_locked?: ss_locked?(swaps),
      kanto_locked?: kanto_locked?(swaps),
      goldenrod_locked?: goldenrod_locked?(swaps),
      surf_locked?: surf_locked?(swaps),
      tree_locked?: tree_locked?(swaps)
    }
  end

  # the seed is ss-locked if the `S_S_TICKET` is replaced by any
  # required key item.
  defp ss_locked?(swaps) do
    state_machine = Diet.Stepper.new(LockDetector.Reachability.SSLocked, nil)

    case Diet.Stepper.run(state_machine, {:begin, swaps}) do
      {{:ss_locked, ^swaps}, _} -> true
      {{:ok, ^swaps}, _} -> false
    end
  end

  # the seed is kanto-locked if kanto is inaccessible but any required
  # key item is in kanto.
  defp kanto_locked?(swaps) do
    state_machine = Diet.Stepper.new(LockDetector.Reachability.KantoLocked, nil)

    case Diet.Stepper.run(state_machine, {:begin, swaps}) do
      {{:kanto_locked, ^swaps}, _} -> true
      {{:ok, ^swaps}, _} -> false
    end
  end

  defp goldenrod_locked?(swaps) do
    state_machine = Diet.Stepper.new(LockDetector.Reachability.GoldenrodLocked, nil)

    case Diet.Stepper.run(state_machine, {:begin, swaps}) do
      {{:goldenrod_locked, ^swaps}, _} -> true
      {{:ok, ^swaps}, _} -> false
    end
  end

  defp surf_locked?(swaps) do
    state_machine = Diet.Stepper.new(LockDetector.Reachability.SurfLocked, nil)

    case Diet.Stepper.run(state_machine, {:begin, swaps}) do
      {{:surf_locked, ^swaps}, _} -> true
      {{:ok, ^swaps}, _} -> false
    end
  end

  defp tree_locked?(swaps) do
    state_machine = Diet.Stepper.new(LockDetector.Reachability.TreeLocked, nil)

    case Diet.Stepper.run(state_machine, {:begin, swaps}) do
      {{:tree_locked, ^swaps}, _} -> true
      {{:ok, ^swaps}, _} -> false
    end
  end
end
